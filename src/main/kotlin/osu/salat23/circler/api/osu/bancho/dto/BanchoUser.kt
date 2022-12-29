package osu.salat23.circler.api.osu.bancho.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class OsuHighestRank(
    val rank: Long,
    @JsonProperty("updated_at")
    val updatedAt: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class BanchoCountry(
    val code: String,
    val name: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OsuUser(
    val id: String,
    val username: String,
    @JsonProperty("is_online")
    val isOnline: Boolean,
    @JsonProperty("has_supporter")
    val hasSupporter: Boolean,
    @JsonProperty("cover_url")
    val coverUrl: String,
    @JsonProperty("avatar_url")
    val avatarUrl: String,
    val country: BanchoCountry,
    @JsonProperty("has_supported")
    val hasSupported: Boolean,
    val playstyle: Array<String>?,
    @JsonProperty("join_date")
    val joinDate: String,
    @JsonProperty("max_blocks")
    val maxBlocks: Long,
    @JsonProperty("max_friends")
    val maxFriends: Long,
    @JsonProperty("post_count")
    val postCount: Long,
    val playmode: String,
    @JsonProperty("rank_highest")
    val highestRank: OsuHighestRank?,
    val statistics: OsuUserStatistics?
)