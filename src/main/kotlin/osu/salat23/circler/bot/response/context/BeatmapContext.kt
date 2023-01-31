package osu.salat23.circler.bot.response.context

import osu.salat23.circler.osu.domain.Beatmap
import osu.salat23.circler.with
import osu.salat23.circler.withDigits
import osu.salat23.circler.bot.response.context.ContextKeys as ckn

class BeatmapContext(
    val beatmap: Beatmap
) : TemplateContext(
    mapOf(
        ckn.OsuBeatmapId.value with beatmap.id,
        ckn.OsuBeatmapApproachRate.value with beatmap.approachRate.withDigits(2),
        ckn.OsuBeatmapCircleSize.value with beatmap.circleSize.withDigits(2),
        ckn.OsuBeatmapHpDrain.value with beatmap.hpDrain.withDigits(2),
        ckn.OsuBeatmapCircleCount.value with beatmap.circleCount.toString(),
        ckn.OsuBeatmapSliderCount.value with beatmap.sliderCount.toString(),
        ckn.OsuBeatmapSpinnerCount.value with beatmap.spinnerCount.toString(),
        ckn.OsuBeatmapMaxCombo.value with beatmap.maxCombo.toString(),
        ckn.OsuBeatmapDifficultyRating.value with beatmap.difficultyRating.withDigits(2),
        ckn.OsuBeatmapAimDifficulty.value with beatmap.aimDifficulty.withDigits(2),
        ckn.OsuBeatmapSpeedDifficulty.value with beatmap.speedDifficulty.withDigits(2),
        ckn.OsuBeatmapBpm.value with beatmap.speedNoteCount.withDigits(2),
        ckn.OsuBeatmapSliderFactor.value with beatmap.sliderFactor.withDigits(2),
        ckn.OsuBeatmapOverallDifficulty.value with beatmap.overallDifficulty.withDigits(2),
        ckn.OsuBeatmapFlashlightDifficulty.value with beatmap.flashlightDifficulty.withDigits(2),
        ckn.OsuBeatmapMode.value with beatmap.mode.displayName,
        ckn.OsuBeatmapStatus.value with beatmap.status.displayName,
        ckn.OsuBeatmapUrl.value with beatmap.url,
    )
)