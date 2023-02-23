package osu.salat23.circler.api.osu.bancho.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BanchoBeatmap (
    val id: Long,
    val accuracy: Double,
    val ar: Double,
    @JsonProperty("beatmapset_id")
    val beatmapsetId: String?,
    val bpm: Double?,
    val convert: Boolean,
    @JsonProperty("count_circles")
    val countCircles: Long,
    val status: String?,
    @JsonProperty("status_int")
    val statusInt: Long,
    // todo rename to sliderCount
    @JsonProperty("count_sliders")
    val countSliders: Long,
    @JsonProperty("count_spinners")
    val countSpinners: Long,
    val cs: Double,
    @JsonProperty("deteled_at")
    val deletedAt: String?,
    val drain: Double,
    val version: String,
    @JsonProperty("difficulty_rating")
    val difficultyRating: Double,
    @JsonProperty("hit_length")
    val hitLength: Long,
    @JsonProperty("is_scorable")
    val isScoreable: Boolean,
    @JsonProperty("last_updated")
    val lastUpdated: String,
    @JsonProperty("mode_int")
    val modeInt: Long,
    val mode: String,
    val passcount: Long,
    val playcount: Long,
    val ranked: Long,
    val url: String,
    @JsonProperty("max_combo")
    val maxCombo: Long,
    val beatmapset: BanchoBeatmapSet?
) {

}