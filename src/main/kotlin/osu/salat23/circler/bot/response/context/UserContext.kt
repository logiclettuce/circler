package osu.salat23.circler.bot.response.context

import osu.salat23.circler.osu.domain.User
import osu.salat23.circler.withDigits
import osu.salat23.circler.bot.response.context.ContextKeys as ckn

class UserContext(
    val user: User
): TemplateContext(
    mapOf(
        Pair(ckn.OsuUserId.value, user.id),
        Pair(ckn.OsuUserUsername.value, user.username),
        Pair(ckn.OsuUserIsOnline.value, user.isOnline.toString()),
        Pair(ckn.OsuUserHasSupporter.value, user.hasSupporter.toString()),
        Pair(ckn.OsuUserMode.value, user.playMode.displayName),
        Pair(ckn.OsuUserAvatar.value, user.avatarUrl),
        Pair(ckn.OsuUserCover.value, user.coverUrl),
        Pair(ckn.OsuUserCountryName.value, user.country.name),
        Pair(ckn.OsuUserCountryCode.value, user.country.code),
        Pair(ckn.OsuUserJoinedDate.value, user.joinDate.toString()),
        Pair(ckn.OsuUserPerformance.value, user.performance.toString()),
        Pair(ckn.OsuUserGlobalRank.value, user.globalRank.toString()),
        Pair(ckn.OsuUserCountryRank.value, user.countryRank.toString()),
        Pair(ckn.OsuUserAccuracy.value, user.accuracy.withDigits(2)),
        Pair(ckn.OsuUserLevel.value, user.level.toString()),
        Pair(ckn.OsuUserLevelProgress.value, user.levelProgress.toString()),
        Pair(ckn.OsuUserPlaycount.value, user.playCount.toString()),
        Pair(ckn.OsuUserPlaytime.value, user.playTime.toString()),
        Pair(ckn.OsuUserMaxCombo.value, user.maximumCombo.toString()),
        Pair(ckn.OsuUserRankedScore.value, user.rankedScore.toString()),
        Pair(ckn.OsuUserTotalScore.value, user.totalScore.toString()),
        Pair(ckn.OsuUserTotalHits.value, user.totalHits.toString()),
        Pair(ckn.OsuUserHighestRank.value, user.highestRank.toString()),
        Pair(ckn.OsuUserHighestRankDate.value, user.toString()) // todo add has highest rank has highest pp etc
    )
)