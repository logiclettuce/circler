package osu.salat23.circler.api.osu.bancho.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OsuScoreStatistics(
    val count_50: Int,
    val count_100: Int,
    val count_300: Int,
    val count_geki: Int,
    val count_katu: Int,
    val count_miss: Int,
) {

}

/*
* Have to implement star rating calculations
* and pp calculation for certain stats
* */

@JsonIgnoreProperties(ignoreUnknown = true)
data class OsuScore(
    val accuracy: Double,
    val best_id: String?,
    val created_at: String,
    val id: String,
    val max_combo: Long,
    val mode: String,
    val mode_int: Int,
    val mods: Array<String>,
// todo hash code for mods and other fields
    val passed: Boolean,
    val perfect: Boolean,
    val pp: Double,
    val rank: String,
    val replay: Boolean,
    val score: Long,
    val statistics: OsuScoreStatistics,
    val beatmap: BanchoBeatmap,
    val beatmapset: BanchoBeatmapSet,
// todo "type":"score_best_osu",
    val user_id: String,
    ) {
    enum class Type {
        Best, Firsts, Recent
    }
}