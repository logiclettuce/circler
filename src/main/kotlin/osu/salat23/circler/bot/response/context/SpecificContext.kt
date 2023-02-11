package osu.salat23.circler.bot.response.context

import com.github.holgerbrandl.jsonbuilder.json
import org.json.JSONObject
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.osu.domain.Score
import osu.salat23.circler.osu.domain.User

object SpecificContext {
    fun userProfileJson(
        user: User,
        server: Server
    ): JSONObject {
        return json {
            "user" to EntityContext.userJson(user)
            "meta" to EntityContext.metaJson(server)
        }
    }

    fun userScoresJson(
        user: User,
        server: Server,
        scores: Collection<Score>
    ): JSONObject {
        val scoresJson = scores.map { EntityContext.scoreJson(it) }.toTypedArray()
        return json {
            "user" to EntityContext.userJson(user)
            "meta" to EntityContext.metaJson(server)
            "scores" to scoresJson
        }
    }
}