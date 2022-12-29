package osu.salat23.circler.osu.formula.performance

import osu.salat23.circler.osu.domain.Beatmap
import osu.salat23.circler.osu.domain.Score

interface PerformanceCalculator {
    fun calculate(score: Score, beatmap: Beatmap): Double
}