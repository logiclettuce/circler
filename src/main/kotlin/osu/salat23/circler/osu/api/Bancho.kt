package osu.salat23.circler.osu.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import osu.salat23.circler.osu.api.domain.models.OsuBeatmapAttributes
import osu.salat23.circler.osu.api.domain.models.OsuScore
import osu.salat23.circler.osu.api.domain.models.OsuUser
import osu.salat23.circler.osu.api.exceptions.OsuUserNotFoundException
import osu.salat23.circler.properties.OsuProperties
import java.io.IOException

@Component
class Bancho (
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

    var logger: Logger = LoggerFactory.getLogger(Bancho::class.java)

    init {
        initToken()
    }

    private fun initToken() {
        val request = Request.Builder()
            .url(BanchoEndpoints.TOKEN_URL)
            .post(
                FormBody.Builder()
                    .add("client_id", properties.clientId)
                    .add("client_secret", properties.clientSecret)
                    .add("grant_type", "client_credentials")
                    .add("scope", "public").build()
            )
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val jsonBody = response.body?.string()
            val tokenData = mapper.readValue(jsonBody, TokenData::class.java)
            this.tokenData = tokenData
        }
    }

    private inline fun <reified T> makeRequest(request: Request): T {
        if (tokenData == null) initToken()
        val newRequest = request.newBuilder().addHeader("Authorization", "${tokenData!!.token_type} ${tokenData!!.access_token}").build()

        client.newCall(newRequest).execute().use { response ->
            if (response.code == 404) throw OsuUserNotFoundException() // todo cha nge to something sane
            else if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val jsonBody = response.body?.string()
            return mapper.readValue(jsonBody, T::class.java)
        }
    }

    override fun user(username: String, osuGameMode: OsuGameMode, key: String?): OsuUser {
        val request = Request.Builder()
            .url(BanchoEndpoints.usersUrl(key ?: username, ))
            .get()
            .build();
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
            .url(BanchoEndpoints.scoresUrl(user.id, type, osuGameMode, pageSize, pageNumber, showFailed))
            .get()
            .build()
        val osuScores: Array<OsuScore> = makeRequest(request);
        return osuScores
    }

    override fun beatmapAttributes(
        beatmapId: Long,
        mods: Int,
        osuGameMode: OsuGameMode
    ): OsuBeatmapAttributes {

        val jsonBody = """
            {
                "mods": $mods,
                ${if (osuGameMode != OsuGameMode.UserDefault) osuGameMode.rulesetId else ""}
            }
        """.trimIndent()
        val request = Request.Builder()
            .url(BanchoEndpoints.beatmapAttributesUrl(beatmapId, mods))
            .post(
                jsonBody.toRequestBody()
            )
            .build()
        return makeRequest(request)
    }
}