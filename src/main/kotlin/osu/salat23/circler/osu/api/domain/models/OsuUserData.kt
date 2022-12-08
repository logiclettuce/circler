package osu.salat23.circler.osu.api.domain.models

data class OsuUserData(
    val id: String,
    val username: String,
    val pp: Long,
    val rank: Long,
    val accuracy: Float,
    val playcount: Long,
    val level: Long,
    val playtime: String,
    val profileLink: String,
    val statistics: OsuUserStatistics
)