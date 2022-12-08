package osu.salat23.circler.osu.api.domain.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OsuBeatmap (
    val id: Long,
    val accuracy: Float,
    val ar: Float,
    val beatmapset_id: String,
    val bpm: Float?,
    val convert: Boolean,
    val count_circles: Int,
    val count_sliders: Int,
    val count_spinners: Int,
    val cs: Float,
    val deleted_at: String?, // todo timestamp better?
    val drain: Float,
    val difficulty_rating: Float,
    val hit_length: Int,
    val is_scoreable: Boolean,
    val last_updated: String, // todo also timestamp
    val mode_int: Int,
    val passcount: Int,
    val playcount: Int,
    val ranked: Int,
    val url: String
)