package osu.salat23.circler.osu.api.domain.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class OsuBeatmapAttributes(
    val max_combo: Int,
    @JsonProperty("star_rating")
    val starRating: Float,
    @JsonProperty("aim_difficulty")
    val aimDifficulty: Float,
    @JsonProperty("approach_rate")
    val approachRate: Float,
    @JsonProperty("flashlight_difficulty")
    val flashlightDifficulty: Float,
    @JsonProperty("overall_difficulty")
    val overallDifficulty: Float,
    @JsonProperty("slider_factor")
    val sliderFactor: Float,
    @JsonProperty("speed_difficulty")
    val speedDifficulty: Float
)