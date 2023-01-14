package osu.salat23.circler.api.osu

import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.bancho.dto.BanchoBeatmapAttributes
import osu.salat23.circler.api.osu.bancho.dto.OsuScore
import osu.salat23.circler.api.osu.bancho.dto.OsuUser
import osu.salat23.circler.osu.domain.Beatmap
import osu.salat23.circler.osu.domain.Mod
import osu.salat23.circler.osu.domain.Score
import osu.salat23.circler.osu.domain.User

@Component
interface OsuApi {
    fun user(username: String, osuGameMode: OsuGameMode = OsuGameMode.UserDefault, key: String? = null): User
    fun userScores(username: String, type: OsuScore.Type, pageSize: Int, pageNumber: Int, osuGameMode: OsuGameMode = OsuGameMode.UserDefault, showFailed: Boolean = true, key: String? = null): Array<Score>

    fun beatmap(id: String, mods: Array<Mod> = arrayOf()): Beatmap
    fun userRecentV1(username: String) {

    }
    fun playerExists(identifier: String): Boolean
}