package osu.salat23.circler.osu.domain

import java.time.LocalDateTime

data class Score(
    val id: String,
    val score: Long,
    var performance: Double,
    var performanceIdeal: Double,
    var performancePerfect: Double,
    val accuracy: Double,
    val maxCombo: Long,
    val date: LocalDateTime,
    val mode: Mode,
    val rank: Rank,
    val globalRank: Long,
    val countryRank: Long,
    val hitPerfect: Long,
    val hitOk: Long,
    val hitMeh: Long,
    val hitMiss: Long,
    val mods: List<Mod>,
    val beatmap: Beatmap
) {

}