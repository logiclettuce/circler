package osu.salat23.circler.api.osu

import org.springframework.stereotype.Component
import osu.salat23.circler.osu.domain.*

@Component
interface OsuApi {
    // todo some work of standardizing method signatures
    fun user(
        identifier: String,
        gameMode: Mode
    ): User
    fun userScores(
        identifier: String,
        gameMode: Mode,
        type: ScoreType,
        pageSize: Long,
        pageNumber: Long,
        showFailed: Boolean
    ): List<Score>
    fun beatmap(
        id: String,
        gameMode: Mode,
        mods: List<Mod>
    ): Beatmap
    fun userBeatmapScores(
        identifier: String,
        gameMode: Mode,
        beatmapId: String,
        requiredMods: List<Mod>,
    ): List<Score>
    fun playerExists(
        identifier: String,
        gameMode: Mode
    ): Boolean
}