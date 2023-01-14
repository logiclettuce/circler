package osu.salat23.circler.osu.domain

import java.time.LocalDate

data class User(
    val id: String,
    val username: String,
    val isOnline: Boolean,
    val hasSupporter: Boolean,
    val playMode: Mode,
    val avatarUrl: String,
    val coverUrl: String,
    val country: Country,
    val joinDate: LocalDate,
    //val lastVisit: LocalDate,
    val performance: Long,
    val globalRank: Long,
    val countryRank: Long,
    val accuracy: Double,
    val level: Long,
    val levelProgress: Long,
    val playCount: Long,
    val playTime: Long,
    val maximumCombo: Long,
    val rankedScore: Long,
    val totalScore: Long,
    val totalHits: Long,
    val highestRank: Long,
    val highestRankDate: LocalDate
)