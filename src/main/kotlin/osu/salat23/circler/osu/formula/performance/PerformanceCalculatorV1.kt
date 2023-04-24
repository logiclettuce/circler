package osu.salat23.circler.osu.formula.performance

import org.springframework.stereotype.Component
import osu.salat23.circler.clamp
import osu.salat23.circler.osu.domain.Beatmap
import osu.salat23.circler.osu.domain.Mod
import osu.salat23.circler.osu.domain.Score

@Component
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

    private fun calculateAccuracy(hitPerfect: Long, hitOk: Long, hitMeh: Long, hitMiss: Long): Double {
        val totalHits = (hitPerfect + hitOk + hitMeh + hitMiss).toDouble() * 300
        val upper = (hitPerfect*300 + hitOk*100 + hitMeh*50).toDouble()
        val calculatedAccuracy = upper / totalHits
        return calculatedAccuracy
    }

    override fun calculate(
        score: Score, beatmap: Beatmap, type: PerformanceCalculator.CalculationType
    ): Double {

        val scoreAttributes = when(type) {
            PerformanceCalculator.CalculationType.Default -> ScoreAttributes(
                score.accuracy,
                score.maxCombo,
                score.hitPerfect,
                score.hitOk,
                score.hitMeh,
                score.hitMiss,
            )
            PerformanceCalculator.CalculationType.Ideal -> ScoreAttributes(
                calculateAccuracy(score.hitPerfect + score.hitMiss, score.hitOk, score.hitMeh, 0),
                beatmap.maxCombo,
                score.hitPerfect + score.hitMiss,
                score.hitOk,
                score.hitMeh,
                0,
            )
            PerformanceCalculator.CalculationType.Perfect -> ScoreAttributes(
                1.0,
                beatmap.maxCombo,
                score.hitPerfect + score.hitOk + score.hitMeh + score.hitMiss,
                0,
                0,
                0,
            )
        }
        val totalHits =
            scoreAttributes.countMiss +
                    scoreAttributes.countMeh +
                    scoreAttributes.countOk +
                    scoreAttributes.countGreat

        var effectiveMissCount = calculateEffectiveMissCount(beatmap, scoreAttributes)

        var multiplier = PERFORMANCE_BASE_MULTIPLIER

        if (score.mods.contains(Mod.NoFail)) {
            multiplier *= Math.max(0.90, 1.0 - 0.02 * effectiveMissCount);
        }
        if (score.mods.contains(Mod.SpunOut) && totalHits > 0) {
            multiplier *= 1.0 - Math.pow(beatmap.spinnerCount.toDouble() / totalHits, 0.85)
        }
        if (score.mods.contains(Mod.Relax)) {
            val okMultiplier = Math.max(
                0.0,
                if (beatmap.overallDifficulty > 0.0) 1 - Math.pow(
                    beatmap.overallDifficulty / 13.33,
                    1.8
                ) else 1.0
            )
            val mehMultiplier = Math.max(
                0.0,
                if (beatmap.overallDifficulty > 0.0) 1 - Math.pow(
                    beatmap.overallDifficulty / 13.33,
                    5.0
                ) else 1.0
            )

            effectiveMissCount = Math.min(
                effectiveMissCount + scoreAttributes.countOk * okMultiplier + scoreAttributes.countMeh * mehMultiplier,
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

        val totalValue = Math.pow(
                    Math.pow(aimValue, 1.1) +
                    Math.pow(speedValue, 1.1) +
                    Math.pow(accuracyValue, 1.1) +
                    Math.pow(flashlightValue, 1.1), 1.0 / 1.1
        ) * multiplier

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

        var flashLightValue = Math.pow(beatmap.flashlightDifficulty, 2.0) * 25.0

        if (effectiveMissCount > 0)
            flashLightValue *= 0.97 * Math.pow(1.0 - Math.pow(effectiveMissCount / totalHits, 0.775), Math.pow(effectiveMissCount, 0.875))

        flashLightValue *= getComboScalingFactor(beatmap, score)

        flashLightValue *= 0.7 + 0.1 * Math.min(1.0, totalHits / 200.0) + if (totalHits > 200) 0.2 * Math.min(
            1.0,
            (totalHits - 200) / 200.0
        ) else 0.0

        flashLightValue *= 0.5 + score.accuracy / 2.0

        flashLightValue *= 0.98 + Math.pow(beatmap.overallDifficulty, 2.0) / 2500.0

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

        val amountHitObjectsWithAccuracy: Long = beatmap.circleCount

        if (amountHitObjectsWithAccuracy > 0)
            betterAccuracyPercentage = ((scoreAttributes.countGreat - (totalHits - amountHitObjectsWithAccuracy)) * 6.0 + scoreAttributes.countOk * 2.0 + scoreAttributes.countMeh) / (amountHitObjectsWithAccuracy * 6).toDouble()
        else
            betterAccuracyPercentage = 0.0

        if (betterAccuracyPercentage < 0)
            betterAccuracyPercentage = 0.0


        var accuracyValue = Math.pow(1.52163, beatmap.overallDifficulty) * Math.pow(betterAccuracyPercentage, 24.0) * 2.83

        accuracyValue *= Math.min(1.15, Math.pow(amountHitObjectsWithAccuracy / 1000.0, 0.3))

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

        var speedValue = Math.pow(5.0 * Math.max(1.0, beatmap.speedDifficulty / 0.0675) - 4.0, 3.0) / 100_000.0;

        val lengthBonus = 0.95 + 0.4 * Math.min(1.0, totalHits / 2000.0) +
                if (totalHits > 2000) Math.log10(totalHits / 2000.0) * 0.5 else 0.0

        speedValue *= lengthBonus

        if (effectiveMissCount > 0) {
            speedValue *=  0.97 * Math.pow(1 - Math.pow(effectiveMissCount / totalHits, 0.775), Math.pow(effectiveMissCount, .875))
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
        val relevantCountGreat = Math.max(0.0, scoreAttributes.countGreat - relevantTotalDiff)
        val relevantCountOk = Math.max(0.0, scoreAttributes.countOk - Math.max(0.0, relevantTotalDiff - scoreAttributes.countGreat))
        val relevantCountMeh = Math.max(0.0, scoreAttributes.countMeh - Math.max(0.0, relevantTotalDiff - scoreAttributes.countGreat - scoreAttributes.countOk))
        val relevantAccuracy = if (beatmap.speedNoteCount == 0.0) 0.0 else ((relevantCountGreat * 6.0 + relevantCountOk * 2.0 + relevantCountMeh) / (beatmap.speedNoteCount * 6.0))


        speedValue *= (0.95 + Math.pow(beatmap.overallDifficulty, 2.0) / 750.0) * Math.pow((scoreAttributes.accuracy + relevantAccuracy) / 2.0, (14.5 - Math.max(beatmap.overallDifficulty, 8.0)) / 2.0)

        speedValue *= Math.pow(0.99, if (scoreAttributes.countMeh < totalHits / 500.0) 0.0 else scoreAttributes.countMeh - totalHits / 500.0)

        return speedValue
    }

    private fun computeAimValue(
        score: Score,
        beatmap: Beatmap,
        scoreAttributes: ScoreAttributes,
        totalHits: Long,
        effectiveMissCount: Double
    ): Double {
        var aimValue = Math.pow(5.0 * Math.max(1.0, beatmap.aimDifficulty / 0.0675) - 4.0, 3.0) / 100_000.0

        val lengthBonus = 0.95 + 0.4 * Math.min(1.0, totalHits / 2000.0) +
                (if (totalHits > 2000) Math.log10(totalHits / 2000.0) * 0.5 else 0.0)
        aimValue *= lengthBonus

        if (effectiveMissCount > 0) {
            aimValue *= 0.97 * Math.pow(1 - Math.pow(effectiveMissCount / totalHits, 0.775), effectiveMissCount)
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
                aimValue *= 1.3 + (totalHits * (0.0016 / (1 + 2 * effectiveMissCount)) * Math.pow(accuracy, 16)) * (1 - 0.003 * attributes.DrainRate * attributes.DrainRate);
        else if (hidden mod) {
         */

        if (score.mods.contains(Mod.Hidden)) {
            aimValue *= 1.0 + 0.04 * (12.0 - beatmap.approachRate)
        }

        val estimateDifficultSliders = beatmap.sliderCount * 0.15

        if (beatmap.sliderCount > 0) {
            val estimateSliderEndsDropped: Double = clamp(Math.min(scoreAttributes.countOk.toDouble() + scoreAttributes.countMeh + scoreAttributes.countMiss, beatmap.maxCombo.toDouble() - scoreAttributes.maxCombo), 0.0, estimateDifficultSliders)
            val sliderNerfFactor = (1 - beatmap.sliderFactor) * Math.pow(1 - estimateSliderEndsDropped / estimateDifficultSliders, 3.0) + beatmap.sliderFactor;
            aimValue *= sliderNerfFactor
        }

        aimValue *= score.accuracy
        aimValue *= 0.98 + Math.pow(beatmap.overallDifficulty, 2.0) / 2500;

        return aimValue
    }

    private fun getComboScalingFactor(beatmap: Beatmap, score: Score): Double {
        return if (score.maxCombo <= 0) 1.0 else Math.min(
            Math.pow(
                score.maxCombo.toDouble(),
                0.8
            ) / Math.pow(beatmap.maxCombo.toDouble(), 0.8), 1.0
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
            if (scoreAttributes.maxCombo < fullComboThreshold) comboBasedMissCount = fullComboThreshold / Math.max(1, scoreAttributes.maxCombo)
        }

        // Clamp miss count to maximum amount of possible breaks
        comboBasedMissCount = Math.min(comboBasedMissCount, (scoreAttributes.countOk + scoreAttributes.countMeh + scoreAttributes.countMiss).toDouble())
        return Math.max(scoreAttributes.countMiss.toDouble(), comboBasedMissCount)
    }
}
