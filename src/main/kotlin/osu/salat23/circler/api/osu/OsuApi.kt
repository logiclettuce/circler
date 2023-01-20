package osu.salat23.circler.api.osu

import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.bancho.dto.BanchoScore
import osu.salat23.circler.osu.domain.Beatmap
import osu.salat23.circler.osu.domain.Mod
import osu.salat23.circler.osu.domain.Score
import osu.salat23.circler.osu.domain.User

@Component
interface OsuApi {
    // todo some work of standardizing method signatures
    fun user(identifier: String, osuGameMode: OsuGameMode = OsuGameMode.UserDefault, key: String? = null): User
    fun userScores(identifier: String, type: BanchoScore.Type, pageSize: Long, pageNumber: Long, osuGameMode: OsuGameMode = OsuGameMode.UserDefault, showFailed: Boolean = true, key: String? = null): Array<Score>

    fun beatmap(id: String, mods: Array<Mod> = arrayOf()): Beatmap
    fun userBeatmapScores(identifier: String, beatmapId: String, requiredMods: List<Mod> = emptyList()): List<Score>
    fun playerExists(identifier: String): Boolean
}