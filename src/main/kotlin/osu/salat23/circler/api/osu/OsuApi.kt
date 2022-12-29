package osu.salat23.circler.api.osu

import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.bancho.dto.BanchoBeatmapAttributes
import osu.salat23.circler.api.osu.bancho.dto.OsuScore
import osu.salat23.circler.api.osu.bancho.dto.OsuUser
import osu.salat23.circler.osu.domain.Beatmap
import osu.salat23.circler.osu.domain.Mod

@Component
interface OsuApi {
    fun user(username: String, osuGameMode: OsuGameMode = OsuGameMode.UserDefault, key: String? = null): OsuUser
    fun userScores(username: String, type: OsuScore.Type, pageSize: Int, pageNumber: Int, osuGameMode: OsuGameMode = OsuGameMode.UserDefault, showFailed: Boolean = false, key: String? = null): Array<OsuScore>

    fun beatmap(id: String, mods: Array<Mod> = arrayOf()): Beatmap
    fun userRecentV1(username: String) {

    }
    fun playerExists(identifier: String): Boolean
}