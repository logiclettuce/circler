package osu.salat23.circler.api.osu.bancho

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.BanchoEndpoints
import osu.salat23.circler.api.osu.OsuApi
import osu.salat23.circler.api.osu.OsuGameMode
import osu.salat23.circler.api.osu.bancho.dto.*
import osu.salat23.circler.api.osu.exceptions.RequestFailedException
import osu.salat23.circler.api.osu.exceptions.UnexpectedException
import osu.salat23.circler.osu.domain.*
import osu.salat23.circler.osu.formula.performance.PerformanceCalculator
import osu.salat23.circler.properties.OsuProperties
import java.io.IOException
import kotlin.math.log

@Component
class BanchoApi(
    private val properties: OsuProperties,
    private val performanceCalculator: PerformanceCalculator
) : OsuApi {

    data class TokenData(
        val access_token: String,
        val token_type: String,
        val expires_in: Int,
    )

    private var tokenData: TokenData? = null;
    private val mapper = jacksonObjectMapper()
    private val client = OkHttpClient()
    private var logger: Logger = LoggerFactory.getLogger(BanchoApi::class.java)

    init {
        initToken()
    }

    private fun initToken() {
        val request = Request.Builder().url(BanchoEndpoints.TOKEN_URL).post(
            FormBody.Builder().add("client_id", properties.clientId).add("client_secret", properties.clientSecret)
                .add("grant_type", "client_credentials").add("scope", "public").build()
        ).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val jsonBody = response.body?.string()
            val tokenData = mapper.readValue(jsonBody, TokenData::class.java)
            this.tokenData = tokenData
        }
    }

    private inline fun <reified T> makeRequest(request: Request, printResult: Boolean = false): T {
        if (tokenData == null) initToken()
        val newRequest =
            request.newBuilder().addHeader("Authorization", "${tokenData!!.token_type} ${tokenData!!.access_token}")
                .build()

        client.newCall(newRequest).execute().use { response ->
            if (!response.isSuccessful) {
                logger.error(response.code.toString())
                throw RequestFailedException(response.code, response.message)
            }

            val jsonBody = response.body?.string()
            if (printResult) logger.info(jsonBody)
            return mapper.readValue(jsonBody, T::class.java)
        }
    }

    override fun user(identifier: String, osuGameMode: OsuGameMode, key: String?): User {
        val request = Request.Builder().url(BanchoEndpoints.usersUrl(key ?: identifier)).get().build();
        return Converter.convert(makeRequest<OsuUser>(request))
    }

    override fun userScores(
        identifier: String,
        type: BanchoScore.Type,
        pageSize: Int,
        pageNumber: Int,
        osuGameMode: OsuGameMode,
        showFailed: Boolean,
        key: String?
    ): Array<Score> {
        val user = user(identifier, osuGameMode, key)
        val request = Request.Builder()
            .url(BanchoEndpoints.scoresUrl(user.id, type, osuGameMode, pageSize, pageNumber, showFailed)).get().build()
        val scoresBancho = makeRequest<Array<BanchoScore>>(request)
        val scores = scoresBancho.map { banchoScore ->
            val banchoBeatmap = banchoBeatmap(banchoScore.beatmap!!.id.toString(), Mod.fromStringArray(banchoScore.mods))
            val banchoBeatmapAttributes = banchoBeatmapAttributes(banchoScore.beatmap!!.id.toString(), Mod.fromStringArray(banchoScore.mods))
            val beatmap = Converter.convert(banchoBeatmap, banchoBeatmapAttributes, banchoBeatmap.beatmapset)
            val score = Converter.convert(banchoScore, beatmap, performanceCalculator)
            return@map score
        }
        return scores.toTypedArray()
    }



    override fun beatmap(
        id: String,
        mods: Array<Mod>
    ): Beatmap {
        val banchoBeatmap: BanchoBeatmap = banchoBeatmap(id, mods)
        val banchoBeatmapAttributes = banchoBeatmapAttributes(banchoBeatmap.id.toString(), mods,/*todo: Gamemode here*/)
        return Converter.convert(banchoBeatmap, banchoBeatmapAttributes, banchoBeatmap.beatmapset)
    }

    override fun userBeatmapScores(identifier: String, beatmapId: String, requiredMods: List<Mod>): List<Score> {
        val user = user(identifier)
        val request = Request.Builder()
            .url(BanchoEndpoints.userBeatmapScores(user.id, beatmapId))
            .get()
            .build()
        // todo refactor this entire file, changing osuScores to banchoScores probably
        val scores = makeRequest<BanchoScores>(request, true).scores.map { osuScore ->
            val beatmap = beatmap(beatmapId, Mod.fromStringArray(osuScore.mods))
            return@map Converter.convert(osuScore, beatmap, performanceCalculator)
        }

        return scores
    }

    private fun banchoBeatmap(
        id: String,
        mods: Array<Mod>
    ): BanchoBeatmap {
        val request = Request.Builder()
            .url(BanchoEndpoints.beatmap(id)).build()
        return makeRequest(request)
    }

    private fun banchoBeatmapAttributes(
        beatmapId: String,
        mods: Array<Mod>,
        osuGameMode: Mode = Mode.Default
    ): BanchoBeatmapAttributes {
        // ${JSONArray(mods.map { mod -> mod.alternativeName })}
        val modsValue = if (mods.size > 0) mods.map { mod -> mod.id }.reduce { acc, id -> acc.or(id) } else 0
        val bodyParameters: RequestBody = FormBody.Builder()
            .add("mods", "${modsValue}")
            .build()
        val request = Request.Builder().url(BanchoEndpoints.beatmapAttributesUrl(beatmapId)).post(
            bodyParameters
        ).build()
        return makeRequest<BanchoBeatmapAttributesWrapper>(request).banchoBeatmapAttributes
    }

    override fun playerExists(identifier: String): Boolean {
        try {
            user(identifier)
        } catch (exception: RequestFailedException) {
            if (exception.code == 404) {
                return false;
            }
            throw UnexpectedException()
        }
        return true;
    }
}