package osu.salat23.circler.renderer.templates

import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.domain.User
import osu.salat23.circler.utility.Time
import java.time.format.DateTimeFormatter
import osu.salat23.circler.renderer.templates.ContextKeyNames as ckn

class ProfileTemplate(val user: User, val command: Command) : ContextTemplate(
    mapOf(
        Pair(ckn.osuCover, user.coverUrl),
        Pair(ckn.osuAvatar, user.avatarUrl),
        Pair(ckn.osuUsername, user.username),
        Pair(ckn.osuServer, command.server.displayName),
        Pair(ckn.osuMode, user.playMode.alternativeName),
        Pair(ckn.osuPlaycount, user.playCount.toString()),
        Pair(ckn.osuPerformance, user.performance.toString()),
        Pair(ckn.osuAccuracy, user.accuracy.toString()),
        Pair(ckn.osuMaxCombo, user.maximumCombo.toString()),
        Pair(ckn.osuPlaytime, Time.fromSecondsToHMS(user.playTime)),
        Pair(ckn.osuJoinedDate, user.joinDate.format(DateTimeFormatter.ISO_DATE)),
    )
)