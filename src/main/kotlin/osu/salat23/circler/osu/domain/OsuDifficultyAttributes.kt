package osu.salat23.circler.osu.domain

class OsuDifficultyAttributes(
    val aimDifficulty: Double,
    val speedDifficulty: Double,
    val speedNoteCount: Double,
    val flashlightDifficulty: Double,
    val sliderFactor: Double,
    val approachRate: Double,
    val overallDifficulty: Double,
    val drainRate: Double,
    val hitCircleCount: Long,
    val sliderCount: Long,
    val spinnerCount: Long,
    starRating: Double,
    maxCombo: Long,
    mods: List<Mod>,
): DifficultyAttributes(
    starRating = starRating,
    maxCombo = maxCombo,
    mods = mods
)