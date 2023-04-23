package osu.salat23.circler.api.osu.bancho

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.runBlocking
import net.kusik.coroutines.transformations.map.mapParallel
import okhttp3.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.BanchoEndpoints
import osu.salat23.circler.api.osu.OsuApi
import osu.salat23.circler.api.osu.ScoreType
import osu.salat23.circler.api.osu.bancho.dto.*
import osu.salat23.circler.api.osu.exceptions.RequestFailedException
import osu.salat23.circler.api.osu.exceptions.UnexpectedException
import osu.salat23.circler.osu.domain.*
import osu.salat23.circler.osu.formula.performance.PerformanceCalculator
import osu.salat23.circler.properties.OsuProperties
import java.io.IOException
// todo refactor every possible signature to include all necessary parameters
@Component
class BanchoApi(
    private val properties: OsuProperties,
    private val performanceCalculator: PerformanceCalculator
) : OsuApi {

    data class TokenData(
        @JsonProperty("access_token")
        val accessToken: String,
        @JsonProperty("token_type")
        val tokenType: String,
        @JsonProperty("expires_in")
        val expiresIn: Int,
    )

    private var tokenData: TokenData? = null
    private val mapper = jacksonObjectMapper()
    private val client = OkHttpClient()
    private var logger: Logger = LoggerFactory.getLogger(BanchoApi::class.java)

    init {
        initToken()
    }

    private fun initToken() {
        val request = Request.Builder().url(BanchoEndpoints.TOKEN_URL).post(
            FormBody.Builder()
                .add("client_id", properties.clientId)
                .add("client_secret", properties.clientSecret)
                .add("grant_type", "client_credentials")
                .add("scope", "public")
                .build()
        ).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val jsonBody = response.body?.string()
            val tokenData = mapper.readValue(jsonBody, TokenData::class.java)
            this.tokenData = tokenData
        }
    }

    private inline fun <reified T> makeRequest(request: Request, printResult: Boolean = false): T {
        run request@{
            if (tokenData == null) initToken()
            val newRequest =
                request.newBuilder().addHeader("Authorization", "${tokenData!!.tokenType} ${tokenData!!.accessToken}")
                    .build()

            client.newCall(newRequest).execute().use { response ->
                if (!response.isSuccessful) {
                    logger.error(response.code.toString())
                    if (response.code == 401) {
                        tokenData = null
                        return@request
                    }
                    throw RequestFailedException(response.code, response.message)
                }

                val jsonBody = response.body?.string()
                if (printResult) logger.info(jsonBody)
                return mapper.readValue(jsonBody, T::class.java)
            }
        }
        throw RequestFailedException(-1, "Could not make the request")
    }

    private fun banchoBeatmap(
        id: String,
    ): BanchoBeatmap {
        val request = Request.Builder()
            .url(BanchoEndpoints.beatmap(id)).build()
        return makeRequest(request)
    }

    private fun banchoBeatmapAttributes(
        beatmapId: String,
        gameMode: Mode,
        mods: List<Mod>,
    ): BanchoOsuBeatmapAttributes {
        val modsValue =
            if (mods.isNotEmpty()) mods
                .map { mod -> mod.id }
                .reduce { acc, id -> acc.or(id) }
            else 0

        val bodyParameters: RequestBody =
            FormBody.Builder()
            .add("mods", "$modsValue")
            .apply { if (gameMode != Mode.Default) this.add("ruleset", gameMode.alternativeName) }
            .build()

        val request =
            Request.Builder()
            .url(BanchoEndpoints.beatmapAttributesUrl(beatmapId))
            .post(bodyParameters)
            .build()

        return makeRequest<BanchoBeatmapAttributesWrapper>(request).banchoBeatmapAttributes
    }

    override fun user(
        identifier: String,
        gameMode: Mode
    ): User {
        val request = Request.Builder().url(BanchoEndpoints.usersUrl(identifier = identifier, mode = gameMode)).get().build()
        return Converter.convertToUser(makeRequest(request))
    }

    override fun userScores(
        identifier: String,
        gameMode: Mode,
        type: ScoreType,
        pageSize: Long,
        pageNumber: Long,
        showFailed: Boolean
    ): List<Score> {
        val user = user(
            identifier = identifier,
            gameMode = gameMode)
        val request = Request.Builder()
            .url(BanchoEndpoints.scoresUrl(
                identifier = user.id,
                type = type,
                mode = gameMode,
                limit = pageSize,
                offset = pageNumber,
                showFailed = showFailed
            )).get().build()
        val scoresBancho = makeRequest<Array<BanchoScore>>(request)
        val scores: List<Score>
        runBlocking {
            scores = scoresBancho.mapParallel { banchoScore ->
                val banchoBeatmap = banchoBeatmap(banchoScore.beatmap!!.id.toString())
                val banchoBeatmapAttributes = banchoBeatmapAttributes(
                    beatmapId = banchoScore.beatmap.id.toString(),
                    gameMode = gameMode,
                    mods = Mod.fromStringList(banchoScore.mods)
                )
                val beatmap = Converter.convertToBeatmap(
                    beatmap = banchoBeatmap,
                    beatmapAttributes = banchoBeatmapAttributes,
                    beatmapSet = banchoBeatmap.beatmapset
                )
                val score = Converter.convertToScore(
                    score = banchoScore,
                    beatmap = beatmap,
                    performanceCalculator = performanceCalculator
                )
                return@mapParallel score
            }
        }
        return scores
    }

    override fun beatmap(
        id: String,
        gameMode: Mode,
        mods: List<Mod>
    ): Beatmap {
        val banchoBeatmap: BanchoBeatmap = banchoBeatmap(id)
        val banchoBeatmapAttributes = banchoBeatmapAttributes(
            beatmapId = banchoBeatmap.id.toString(),
            gameMode = gameMode,
            mods = mods)
        return Converter.convertToBeatmap(
            beatmap = banchoBeatmap,
            beatmapAttributes = banchoBeatmapAttributes,
            beatmapSet = banchoBeatmap.beatmapset)
    }

    override fun userBeatmapScores(
        identifier: String,
        gameMode: Mode,
        beatmapId: String,
        requiredMods: List<Mod>
    ): List<Score> {
        val user = user(identifier, gameMode)
        val request = Request.Builder()
            .url(BanchoEndpoints.userBeatmapScores(user.id, beatmapId))
            .get()
            .build()

        val scores = makeRequest<BanchoScores>(request, true).scores.map { osuScore ->
            val beatmap = beatmap(
                id = beatmapId,
                gameMode = gameMode,
                mods = Mod.fromStringList(osuScore.mods)
            )
            return@map Converter.convertToScore(
                score = osuScore,
                beatmap = beatmap,
                performanceCalculator = performanceCalculator)
        }

        return scores
    }

    override fun playerExists(
        identifier: String,
    ): Boolean {
        try {
            user(
                identifier = identifier
            )
        } catch (exception: RequestFailedException) {
            if (exception.code == 404) {
                return false
            }
            throw UnexpectedException()
        }
        return true
    }
}