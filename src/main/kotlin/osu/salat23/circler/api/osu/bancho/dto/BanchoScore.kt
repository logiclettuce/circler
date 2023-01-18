package osu.salat23.circler.api.osu.bancho.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class OsuScoreStatistics(
    val count_50: Long,
    val count_100: Long,
    val count_300: Long,
    val count_geki: Long,
    val count_katu: Long,
    val count_miss: Long,
) {

}

/*
* Have to implement star rating calculations
* and pp calculation for certain stats
* */

@JsonIgnoreProperties(ignoreUnknown = true)
data class BanchoScores(
    val scores: List<BanchoScore>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class BanchoScore(
    val accuracy: Double,
    val best_id: String?,
    val created_at: String,
    val id: String,
    @JsonProperty("max_combo")
    val maxCombo: Long,
    val mode: String,
    val mode_int: Long,
    val mods: Array<String>,
// todo hash code for mods and other fields
    val passed: Boolean,
    val perfect: Boolean,

    val pp: Double,
    @JsonProperty("rank_global")
    val rankGlobal: Long,
    @JsonProperty("rank_country")
    val rankCountry: Long,
    val rank: String,
    val replay: Boolean,
    val score: Long,
    val statistics: OsuScoreStatistics,
    val beatmap: BanchoBeatmap?,
    val beatmapset: BanchoBeatmapSet?,
// todo "type":"score_best_osu",
    val user_id: String,
    ) {
    enum class Type {
        Best, Firsts, Recent
    }
}