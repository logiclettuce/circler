package osu.salat23.circler.api.osu

import osu.salat23.circler.api.osu.bancho.dto.BanchoScore
import osu.salat23.circler.osu.domain.Mode

object BanchoEndpoints {
    const val TOKEN_URL: String = "https://osu.ppy.sh/oauth/token"
    const val AUTH_URL: String = "https://osu.ppy.sh/oauth/authorize"
    const val OSU_API_BASE_V2 = "https://osu.ppy.sh/api/v2"


    fun usersUrl(identifier: String, mode: Mode) =
        "$OSU_API_BASE_V2/users/${identifier}/${mode.alternativeName}"

    fun beatmap(id: String) = "$OSU_API_BASE_V2/beatmaps/$id"
    // todo make parameters more standardized. e.g. this function should not have default parameters or otherwise with the osuapi interface
    fun scoresUrl(identifier: String, type: ScoreType, mode: Mode, limit: Long, offset: Long, showFailed: Boolean): String {
        val queryParams = mutableListOf(
            if (mode.alternativeName.isNotEmpty()) "mode=${mode.alternativeName}" else "",
            "limit=${limit}",
            "offset=${(if (offset - 1 >= 0) offset-1 else 0) * limit}",
            "include_fails=${if (showFailed) 1 else 0}"
        )

        return "$OSU_API_BASE_V2/users/${identifier}/scores/${type.name.lowercase()}?"+queryParams.filter{ it.isNotEmpty() }.joinToString(separator = "&")
    }

    fun beatmapAttributesUrl(beatmapId: String): String = "$OSU_API_BASE_V2/beatmaps/$beatmapId/attributes"

    fun userBeatmapScores(identifier: String, beatmapId: String) = "$OSU_API_BASE_V2/beatmaps/$beatmapId/scores/users/$identifier/all"

}