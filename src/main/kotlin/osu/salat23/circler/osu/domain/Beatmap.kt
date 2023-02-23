package osu.salat23.circler.osu.domain

data class Beatmap(
    val id: String,
    val approachRate: Double,
    val circleSize: Double,
    val hpDrain: Double,
    val circleCount: Long,
    val sliderCount: Long,
    val spinnerCount: Long,
    val maxCombo: Long,
    val difficultyRating: Double,
    val aimDifficulty: Double,
    val speedDifficulty: Double,
    val speedNoteCount: Double,
    val sliderFactor: Double,
    val overallDifficulty: Double,
    val flashlightDifficulty: Double,
    val mode: Mode,
    val status: Status,
    val url: String,
    val version: String,
    val beatmapSet: BeatmapSet? = null
)