package osu.salat23.circler.api.osu.bancho.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Covers(
    val cover: String?,
    @JsonProperty("cover@2x")
    val cover2x: String?,
    val card: String?,
    @JsonProperty("card@2x")
    val card2x: String?,
    val list: String?,
    @JsonProperty("list@2x")
    val list2x: String?,
    val slimCover: String?,
    @JsonProperty("slimcover@2x")
    val slimCover2x: String?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class BanchoBeatmapSet(
    val artist: String,
    @JsonProperty("artist_unicode")
    val artistUnicode: String,
    val covers: Covers,
    val creator: String,
    @JsonProperty("favourite_count")
    val favouriteCount: Long,
    val id: String,
    val nsfw: Boolean,
    @JsonProperty("play_count")
    val playCount: Long,
    @JsonProperty("preview_url")
    val preview_url: String,
    val source: String,
    val status: String,
    val title: String,
    @JsonProperty("title_unicode")
    val titleUnicode: String,
    @JsonProperty("user_id")
    val userId: String,
    val video: Boolean
)