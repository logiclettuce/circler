package osu.salat23.circler.bot.command.impl

import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default
import osu.salat23.circler.osu.domain.Mode

@Command(
    name = "Chat leaderboard",
    description = "Player leaderboard in the current chat.",
    identifiers = ["leaderboard", "lb", "дуфвукищфкв", "ди"]
)
class ChatLeaderboardCommand(
    @Argument(
        name = "Server",
        description = "Specify game server.",
        identifiers = ["server", "s", "ыукмук", "ы"],
    )
    @Default("bancho")
    var server: Server,

    @Argument(
        name = "Mode",
        description = "Specify game mode.",
        identifiers = ["mode", "m", "ьщву", "ь"],
    )
    @Default("default")
    var gameMode: Mode,
)