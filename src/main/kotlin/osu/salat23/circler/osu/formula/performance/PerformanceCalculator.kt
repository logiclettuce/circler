package osu.salat23.circler.osu.formula.performance

import osu.salat23.circler.osu.domain.Beatmap
import osu.salat23.circler.osu.domain.Score

interface PerformanceCalculator {

    enum class CalculationType {
        Default,
        Ideal,
        Perfect
    }
    fun calculate(score: Score, beatmap: Beatmap, type: CalculationType = CalculationType.Default): Double
}