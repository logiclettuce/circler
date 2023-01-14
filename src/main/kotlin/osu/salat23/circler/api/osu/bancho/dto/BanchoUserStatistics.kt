package osu.salat23.circler.api.osu.bancho.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class OsuUserLevel(
    val current: Long,
    val progress: Long
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OsuUserGradeCounts(
    val ss: Long,
    val ssh: Long,
    val s: Long,
    val sh: Long,
    val a: Long
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OsuUserStatistics(
    val level: OsuUserLevel?,
    val pp: Long,
    @JsonProperty("global_rank")
    val globalRank: Long,
    @JsonProperty("country_rank")
    val countryRank: Long,
    @JsonProperty("ranked_score")
    val rankedScore: Long,
    @JsonProperty("hit_accuracy")
    val hitAccuracy: Double,
    @JsonProperty("play_count")
    val playCount: Long,
    @JsonProperty("play_time")
    val playTime: Long,
    @JsonProperty("total_score")
    val totalScore: Long,
    @JsonProperty("total_hits")
    val totalHits: Long,
    @JsonProperty("maximum_combo")
    val maximumCombo: Long,
    @JsonProperty("replays_watched_by_others")
    val replaysWatchedByOthers: Long,
    @JsonProperty("is_ranked")
    val isRanked: Boolean,
    @JsonProperty("grade_counts")
    val gradeCounts: OsuUserGradeCounts?,
)