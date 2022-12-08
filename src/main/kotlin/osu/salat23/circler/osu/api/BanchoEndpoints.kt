package osu.salat23.circler.osu.api

import osu.salat23.circler.osu.api.domain.models.OsuScore

object BanchoEndpoints {
    const val TOKEN_URL: String = "https://osu.ppy.sh/oauth/token"
    const val AUTH_URL: String = "https://osu.ppy.sh/oauth/authorize"
    const val OSU_API_BASE_V2 = "https://osu.ppy.sh/api/v2"


    fun usersUrl(identifier: String, mode: OsuGameMode = OsuGameMode.UserDefault) =
        "$OSU_API_BASE_V2/users/${identifier}/${mode.value}"

    fun scoresUrl(identifier: String, type: OsuScore.Type, mode: OsuGameMode = OsuGameMode.UserDefault, limit: Int = 5, offset: Int = 0, showFailed: Boolean = false): String {
        val queryParams = mutableListOf(
            if (mode.value.isNotEmpty()) "mode=${mode.value}" else "",
            "limit=${limit}",
            "offset=${(if (offset - 1 >= 0) offset-1 else 0) * limit}",
            "include_fails=${if (showFailed) 1 else 0}"
        )

        return "$OSU_API_BASE_V2/users/${identifier}/scores/${type.name.lowercase()}?"+queryParams.filter{ it.isNotEmpty() }.joinToString(separator = "&")
    }

    fun beatmapAttributesUrl(beatmapId: Long, mods: Int = 0): String = "$OSU_API_BASE_V2/beatmaps/$beatmapId/attributes"

}