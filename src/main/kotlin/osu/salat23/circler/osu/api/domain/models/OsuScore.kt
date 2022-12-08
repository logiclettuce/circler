package osu.salat23.circler.osu.api.domain.models

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
    val accuracy: Float,
    val best_id: String?,
    val created_at: String,
    val id: String,
    val max_combo: Long,
    val mode: String,
    val mode_int: Int,
// todo mods
    val passed: Boolean,
    val perfect: Boolean,
    val pp: Float,
    val rank: String,
    val replay: Boolean,
    val score: Long,
    val statistics: OsuScoreStatistics,
    val beatmap: OsuBeatmap,
    val beatmapset: OsuBeatmapSet,
// todo "type":"score_best_osu",
    val user_id: String,
    ) {
    enum class Type {
        Best, Firsts, Recent
    }
}