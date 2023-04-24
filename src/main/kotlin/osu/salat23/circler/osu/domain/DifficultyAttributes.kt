package osu.salat23.circler.osu.domain

abstract class DifficultyAttributes(
    val starRating: Double,
    val maxCombo: Long,
    val mods: List<Mod>
)