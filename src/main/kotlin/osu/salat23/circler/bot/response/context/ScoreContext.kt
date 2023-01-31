package osu.salat23.circler.bot.response.context

import osu.salat23.circler.osu.domain.Score
import osu.salat23.circler.with
import osu.salat23.circler.withDigits
import osu.salat23.circler.bot.response.context.ContextKeys as ckn

class ScoreContext(
    val score: Score
): TemplateContext(
    mapOf(
        ckn.OsuScoreId.value with score.id,
        ckn.OsuScoreScore.value with score.score.toString(),
        ckn.OsuScorePerformance.value with score.performance.withDigits(2),
        ckn.OsuScoreMaxCombo.value with score.accuracy.withDigits(2),
        ckn.OsuScoreDate.value with score.date.toString(), // todo date format
        ckn.OsuScoreMode.value with score.mode.displayName,
        ckn.OsuScoreRank.value with score.rank.name,
        ckn.OsuScoreGlobalRank.value with score.globalRank.toString(),
        ckn.OsuScoreCountryRank.value with score.countryRank.toString(),
        ckn.OsuScoreHitPerfect.value with score.hitPerfect.toString(),
        ckn.OsuScoreHitOk.value with score.hitOk.toString(),
        ckn.OsuScoreHitMeh.value with score.hitMeh.toString(),
        ckn.OsuScoreHitMiss.value with score.hitMiss.toString(),
        ckn.OsuScoreMods.value with score.mods.sortedBy { it.id }.joinToString("") { it.alternativeName },
    )
)