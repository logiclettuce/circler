package osu.salat23.circler.api.osu.bancho

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.BanchoEndpoints
import osu.salat23.circler.api.osu.OsuApi
import osu.salat23.circler.api.osu.OsuGameMode
import osu.salat23.circler.api.osu.bancho.dto.*
import osu.salat23.circler.api.osu.exceptions.RequestFailedException
import osu.salat23.circler.api.osu.exceptions.UnexpectedException
import osu.salat23.circler.osu.domain.Beatmap
import osu.salat23.circler.osu.domain.Mod
import osu.salat23.circler.osu.domain.Mode
import osu.salat23.circler.properties.OsuProperties
import java.io.IOException

@Component
class BanchoApi(
    private val properties: OsuProperties,
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
            if (!response.isSuccessful) throw RequestFailedException(response.code, response.message)

            val jsonBody = response.body?.string()
            if (printResult) logger.info(jsonBody)
            return mapper.readValue(jsonBody, T::class.java)
        }
    }

    override fun user(username: String, osuGameMode: OsuGameMode, key: String?): OsuUser {
        val request = Request.Builder().url(BanchoEndpoints.usersUrl(key ?: username)).get().build();
        return makeRequest(request)
    }

    override fun userScores(
        username: String,
        type: OsuScore.Type,
        pageSize: Int,
        pageNumber: Int,
        osuGameMode: OsuGameMode,
        showFailed: Boolean,
        key: String?
    ): Array<OsuScore> {
        val user = user(username, osuGameMode, key)
        val request = Request.Builder()
            .url(BanchoEndpoints.scoresUrl(user.id, type, osuGameMode, pageSize, pageNumber, showFailed)).get().build()
        return makeRequest(request)
    }

    override fun beatmap(
        id: String,
        mods: Array<Mod>
    ): Beatmap {
        val request = Request.Builder()
            .url(BanchoEndpoints.beatmap(id)).build()
        val banchoBeatmap: BanchoBeatmap = makeRequest(request)
        val banchoBeatmapAttributes = beatmapAttributes(banchoBeatmap.id, mods,/*todo: Gamemode here*/)
        return Converter.convert(banchoBeatmap, banchoBeatmapAttributes, banchoBeatmap.beatmapset)
    }

    private fun beatmapAttributes(
        beatmapId: Long,
        mods: Array<Mod>,
        osuGameMode: Mode = Mode.Default
    ): BanchoBeatmapAttributes {
        val jsonBody = """
            {
                "mods": ${JSONArray(mods)},
                ${if (osuGameMode != Mode.Default) "mode: ${osuGameMode.alternativeName}" else ""}
            }
        """.trimIndent()
        val request = Request.Builder().url(BanchoEndpoints.beatmapAttributesUrl(beatmapId)).post(
            jsonBody.toRequestBody()
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