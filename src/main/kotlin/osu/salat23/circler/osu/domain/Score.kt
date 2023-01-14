package osu.salat23.circler.osu.domain

import java.time.LocalDate

data class Score(
    val id: String,
    val score: Long,
    var performance: Double,
    val accuracy: Double,
    val maxCombo: Long,
    val date: LocalDate,
    val mode: Mode,
    val rank: Rank,
    val globalRank: Long,
    val countryRank: Long,
    val hitPerfect: Long,
    val hitOk: Long,
    val hitMeh: Long,
    val hitMiss: Long,
    val mods: Array<Mod>,
    val beatmap: Beatmap
) {

}