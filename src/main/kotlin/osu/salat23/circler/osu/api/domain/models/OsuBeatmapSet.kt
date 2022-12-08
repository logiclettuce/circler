package osu.salat23.circler.osu.api.domain.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OsuBeatmapSet(
    val artist: String,
    val artist_unicode: String,
    // todo  val covers:	Covers,
    val creator: String,
    val favourite_count: Long,
    val id: String,
    val nsfw: Boolean,
    val play_count: Long,
    val preview_url: String,
    val source: String,
    val status: String,
    val title: String,
    val title_unicode: String,
    val user_id: String,
    val video: Boolean
)