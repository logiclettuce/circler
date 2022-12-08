package osu.salat23.circler.osu.api

import org.springframework.stereotype.Component
import osu.salat23.circler.osu.api.domain.models.OsuBeatmapAttributes
import osu.salat23.circler.osu.api.domain.models.OsuScore
import osu.salat23.circler.osu.api.domain.models.OsuUser

@Component
interface OsuApi {
    fun user(username: String, osuGameMode: OsuGameMode = OsuGameMode.UserDefault, key: String? = null): OsuUser
    fun userScores(username: String, type: OsuScore.Type, pageSize: Int, pageNumber: Int, osuGameMode: OsuGameMode = OsuGameMode.UserDefault, showFailed: Boolean = false, key: String? = null): Array<OsuScore>
    fun beatmapAttributes(beatmapId: Long, mods: Int, osuGameMode: OsuGameMode): OsuBeatmapAttributes
}