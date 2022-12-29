package osu.salat23.circler.api.osu.bancho.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class BanchoBeatmap (
    val id: Long,
    val accuracy: Double,
    val ar: Double,
    val beatmapset_id: String,
    val bpm: Double?,
    val convert: Boolean,
    val count_circles: Long,
    val status: String,
    val status_int: Long,
    // todo rename to sliderCount
    val count_sliders: Long,
    val count_spinners: Long,
    val cs: Double,
    val deleted_at: String?, // todo timestamp better?
    val drain: Double,
    val difficulty_rating: Double,
    val hit_length: Long,
    val is_scoreable: Boolean,
    val last_updated: String, // todo also timestamp
    val mode_int: Long,
    val mode: String,
    val passcount: Long,
    val playcount: Long,
    val ranked: Long,
    val url: String,
    val max_combo: Long,
    val beatmapset: BanchoBeatmapSet?
) {

}