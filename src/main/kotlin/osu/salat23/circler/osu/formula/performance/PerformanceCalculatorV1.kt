package osu.salat23.circler.osu.formula.performance

import osu.salat23.circler.osu.domain.Beatmap
import osu.salat23.circler.osu.domain.Mod
import osu.salat23.circler.osu.domain.Score
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class PerformanceCalculatorV1 : PerformanceCalculator {

    companion object {
        const val PERFORMANCE_BASE_MULTIPLIER: Double = 1.14;
    }

    private data class ScoreAttributes(
        val accuracy: Double,
        val maxCombo: Long,
        val countGreat: Long,
        val countOk: Long,
        val countMeh: Long,
        val countMiss: Long,
    )

    override fun calculate(
        score: Score, beatmap: Beatmap
        /*score: OsuScore, beatmap: BanchoBeatmap, attributes: BanchoBeatmapAttributes*/
    ): Double {

        val scoreAttributes = ScoreAttributes(
            score.accuracy,
            score.maxCombo,
            score.hitPerfect,
            score.hitOk,
            score.hitMeh,
            score.hitMiss,
        )
        val totalHits =
            scoreAttributes.countMiss +
                    scoreAttributes.countMeh +
                    scoreAttributes.countOk +
                    scoreAttributes.countGreat

        var effectiveMissCount = calculateEffectiveMissCount(beatmap, scoreAttributes)

        var multiplier = PERFORMANCE_BASE_MULTIPLIER
        if (score.mods.contains(Mod.NoFail)) {
            multiplier *= max(0.90, 1.0 - 0.02 * effectiveMissCount)
        }
        if (score.mods.contains(Mod.SpunOut) && totalHits > 0) {
            multiplier *= (beatmap.spinnerCount.toDouble() / totalHits).pow(0.85)
        }
        if (score.mods.contains(Mod.Relax)) {
            val okMultiplier = max(
                0.0,
                if (beatmap.overallDifficulty > 0) (1 - (beatmap.overallDifficulty / 13.33).pow(1.8)) else 1.0
            )
            val mehMultiplier = max(
                0.0,
                if (beatmap.overallDifficulty > 0) (1 - (beatmap.overallDifficulty / 13.33).pow(5)) else 1.0
            )

            effectiveMissCount = min(
                effectiveMissCount + scoreAttributes.countOk * okMultiplier + scoreAttributes.countMeh,
                totalHits.toDouble()
            )
        }

        val aimValue = computeAimValue(score, beatmap, scoreAttributes, totalHits, effectiveMissCount)
        val speedValue: Double =
            computeSpeedValue(score, beatmap, scoreAttributes, totalHits, effectiveMissCount)
        val accuracyValue: Double =
            computeAccuracyValue(score, beatmap, scoreAttributes, totalHits, effectiveMissCount)
        val flashlightValue: Double =
            computeFlashlightValue(score, beatmap, scoreAttributes, totalHits, effectiveMissCount)

        val totalValue = (
                aimValue.pow(1.1) +
                        speedValue.pow(1.1) +
                        accuracyValue.pow(1.1) +
                        flashlightValue.pow(1.1)
                ).pow(1.0 / 1.1) * multiplier

        return totalValue
    }

    private fun computeFlashlightValue(
        score: Score,
        beatmap: Beatmap,
        scoreAttributes: ScoreAttributes,
        totalHits: Long,
        effectiveMissCount: Double
    ): Double {
        val mods = score.mods
        if (!mods.contains(Mod.Flashlight))
            return 0.0

        var flashLightValue = beatmap.flashlightDifficulty.pow(2.0) * 25.0

        if (effectiveMissCount > 0)
            flashLightValue *= 0.97 * (1 - (effectiveMissCount / totalHits).pow(0.775)).pow(effectiveMissCount.pow(0.875))

        flashLightValue *= getComboScalingFactor(beatmap, score)

        flashLightValue *= 0.7 + 0.1 * min(1.0, totalHits / 200.0) +
                (if (totalHits > 200) 0.2 * min(1.0, (totalHits - 200) / 200.0) else 0.0)

        flashLightValue *= 0.5 + score.accuracy / 2.0

        flashLightValue *= 0.98 + beatmap.overallDifficulty.pow(2) / 2500

        return flashLightValue
    }

    private fun computeAccuracyValue(
        score: Score,
        beatmap: Beatmap,
        scoreAttributes: ScoreAttributes,
        totalHits: Long,
        effectiveMissCount: Double
    ): Double {
        val mods = score.mods
        if (mods.contains(Mod.Relax))
            return 0.0

        var betterAccuracyPercentage: Double

        val amountHitObjectsWithAccuracy = beatmap.circleCount

        betterAccuracyPercentage = if (amountHitObjectsWithAccuracy > 0)
            ((scoreAttributes.countGreat - (totalHits - amountHitObjectsWithAccuracy)) * 6 + scoreAttributes.countOk * 2 + scoreAttributes.countMeh) / (amountHitObjectsWithAccuracy * 6).toDouble()
        else
            0.0


        if (betterAccuracyPercentage < 0.0)
            betterAccuracyPercentage = 0.0

        var accuracyValue = 1.52163.pow(beatmap.overallDifficulty) * betterAccuracyPercentage.pow(24) * 2.83

        accuracyValue *= min(1.15, (amountHitObjectsWithAccuracy / 1000.0).pow(0.3))

        /*
        unimplemented Blinds mod here

                if (mods.contains(Mod.Hidden)) {
                    accuracyValue *= 1.14
                } else if (hidden mod) {

         */

        if (mods.contains(Mod.Hidden)) {
            accuracyValue *= 1.08
        }

        if (mods.contains(Mod.Flashlight)) {
            accuracyValue *= 1.02
        }

        return accuracyValue;
    }

    private fun computeSpeedValue(
        score: Score,
        beatmap: Beatmap,
        scoreAttributes: ScoreAttributes,
        totalHits: Long,
        effectiveMissCount: Double
    ): Double {
        if (score.mods.contains(Mod.Relax))
            return 0.0

        var speedValue = (5.0 * max(1.0, beatmap.speedDifficulty / 0.0675) - 4.0).pow(3.0) / 100_000.0

        val lengthBonus = 0.95 + 0.4 * min(1.0, totalHits / 2000.0) +
                (if (totalHits > 2000) log10(totalHits / 2000.0) * 0.5 else 0.0)
        speedValue *= lengthBonus

        if (effectiveMissCount > 0) {
            speedValue *= 0.97 *
                    (1 - (effectiveMissCount / totalHits).pow(0.775).pow((effectiveMissCount.pow(.875))))
        }

        speedValue *= getComboScalingFactor(beatmap, score)

        var approachRateFactor = 0.0
        if (beatmap.approachRate > 10.33) {
            approachRateFactor = 0.3 * (beatmap.approachRate - 10.33)
        }

        speedValue *= 1.0 + approachRateFactor * lengthBonus

        /*
        unimplemented Blinds mod here

                if (mods.contains(Mod.Hidden)) {
                    speedValue *= 1.12
                } else if (hidden mod) {

         */

        if (score.mods.contains(Mod.Hidden)) {
            speedValue *= 1.0 + 0.04 * (12.0 - beatmap.approachRate);
        }

        val relevantTotalDiff = totalHits - beatmap.speedNoteCount
        val relevantCountGreat = max(0.0, scoreAttributes.countGreat - relevantTotalDiff)
        val relevantCountOk =
            max(0.0, scoreAttributes.countOk - max(0.0, relevantTotalDiff - scoreAttributes.countGreat))
        val relevantCountMeh = max(
            0.0,
            scoreAttributes.countMeh - max(
                0.0,
                relevantTotalDiff - scoreAttributes.countGreat - scoreAttributes.countOk
            )
        )
        val relevantAccuracy =
            if (beatmap.speedNoteCount == 0.0) 0.0
            else (relevantCountGreat * 6.0 + relevantCountOk * 2.0 + relevantCountMeh) / (beatmap.speedNoteCount * 6.0)

        speedValue *= (0.95 + beatmap.overallDifficulty.pow(2) / 750) * ((score.accuracy + relevantAccuracy) / 2.0).pow(
            14.5 - max(beatmap.overallDifficulty, 8.0)
        ) / 2

        speedValue *= 0.99.pow(if (scoreAttributes.countMeh < totalHits / 500.0) 0.0 else scoreAttributes.countMeh - totalHits / 500.0)

        return speedValue;
    }

    private fun computeAimValue(
        score: Score,
        beatmap: Beatmap,
        scoreAttributes: ScoreAttributes,
        totalHits: Long,
        effectiveMissCount: Double
    ): Double {
        var aimValue = (5.0 * max(1.0, beatmap.aimDifficulty / 0.0675) - 4.0).pow(3.0) / 100_000.0

        val lengthBonus = 0.95 + 0.4 * min(1.0, totalHits / 2000.0) +
                (if (totalHits > 2000) log10(totalHits / 2000.0) * 0.5 else 0.0)
        aimValue *= lengthBonus

        if (effectiveMissCount > 0) {
            aimValue *= 0.97 * (1 - (effectiveMissCount / totalHits).pow(0.775)).pow(effectiveMissCount)
        }

        aimValue *= getComboScalingFactor(beatmap, score)

        var approachRateFactor = 0.0

        if (beatmap.approachRate > 10.33) {
            approachRateFactor = 0.3 * (beatmap.approachRate - 10.33)
        } else if (beatmap.approachRate < 8.0) {
            approachRateFactor = 0.05 * (8.0 - beatmap.approachRate)
        }

        if (score.mods.contains(Mod.Relax)) {
            approachRateFactor = 0.0
        }

        aimValue *= 1.0 + approachRateFactor * lengthBonus

        /*
        There should be a section with currently unimplemented blinds mod
        if (score.Mods.Any(m => m is OsuModBlinds))
                aimValue *= 1.3 + (totalHits * (0.0016 / (1 + 2 * effectiveMissCount)) * Math.Pow(accuracy, 16)) * (1 - 0.003 * attributes.DrainRate * attributes.DrainRate);
        else if (hidden mod) {
         */

        if (score.mods.contains(Mod.Hidden)) {
            aimValue *= 1.0 + 0.04 * (12.0 - beatmap.approachRate)
        }

        val estimateDifficultSliders = beatmap.sliderCount * 0.15

        if (beatmap.sliderCount > 0) {
            val estimateSliderEndsDropped = min(
                (scoreAttributes.countOk + scoreAttributes.countMeh + scoreAttributes.countMiss),
                beatmap.maxCombo - score.maxCombo
            ).toDouble().coerceIn(0.0, estimateDifficultSliders)
            val sliderNerfFactor =
                (1 - beatmap.sliderFactor) * (1 - estimateSliderEndsDropped / estimateDifficultSliders).pow(3) + beatmap.sliderFactor
            aimValue *= sliderNerfFactor
        }

        aimValue *= score.accuracy

        return aimValue
    }

    private fun getComboScalingFactor(beatmap: Beatmap, score: Score): Double {
        return if (beatmap.maxCombo <= 0) 1.0 else min(
            score.maxCombo.toDouble().pow(0.8) / beatmap.maxCombo.toDouble().pow(0.8), 1.0
        )
    }

    private fun calculateEffectiveMissCount(
        beatmap: Beatmap,
        scoreAttributes: ScoreAttributes
    ): Double {
        // Guess the number of misses + slider breaks from combo
        var comboBasedMissCount = 0.0
        if (beatmap.sliderCount > 0) {
            val fullComboThreshold: Double = beatmap.maxCombo - 0.1 * beatmap.sliderCount
            if (scoreAttributes.maxCombo < fullComboThreshold) comboBasedMissCount =
                fullComboThreshold / 1.0.coerceAtLeast(scoreAttributes.maxCombo.toDouble())
        }

        // Clamp miss count to maximum amount of possible breaks
        comboBasedMissCount =
            comboBasedMissCount.coerceAtMost((scoreAttributes.countOk + scoreAttributes.countMeh + scoreAttributes.countMiss).toDouble())
        return scoreAttributes.countMiss.toDouble().coerceAtLeast(comboBasedMissCount)
    }
}
