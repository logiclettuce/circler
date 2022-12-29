package osu.salat23.circler.api.osu.bancho.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.annotation.JsonValue

@JsonIgnoreProperties(ignoreUnknown = true)
data class BanchoBeatmapAttributesWrapper(
    @JsonProperty("attributes")
    val banchoBeatmapAttributes: BanchoBeatmapAttributes
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class BanchoBeatmapAttributes(
    val max_combo: Long,
    @JsonProperty("star_rating")
    val starRating: Double,
    @JsonProperty("aim_difficulty")
    val aimDifficulty: Double,
    @JsonProperty("approach_rate")
    val approachRate: Double,
    @JsonProperty("flashlight_difficulty")
    val flashlightDifficulty: Double,
    @JsonProperty("overall_difficulty")
    val overallDifficulty: Double,
    @JsonProperty("slider_factor")
    val sliderFactor: Double,
    @JsonProperty("speed_difficulty")
    val speedDifficulty: Double,
    @JsonProperty("speed_note_count")
    val speedNoteCount: Double
)