package osu.salat23.circler.bot.command.impl

import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default
import osu.salat23.circler.osu.domain.Mode

@Command(
    name = "User top scores",
    description = "Gets a list of user top scores.",
    identifiers = ["top", "t", "ещз", "е"]
)
class FetchUserTopScoresCommand(
    @Argument(
        name = "Player",
        description = "Specify player username.",
        identifiers = ["nick", "nickname", "тшсл", "тшслтфьу"],
    )
    @Default("")
    var actor: String,

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

    @Argument(
        name = "Page size",
        description = "Specify amount of elements that result can contain at once.",
        identifiers = ["page", "p", "зфпу", "з"],
    )
    @Default("5")
    var pageSize: Long,

    @Argument(
        name = "Page number",
        description = "Specify the number of page. Used to cycle through large result lists. Starts at 1.",
        identifiers = ["number", "n", "тгьиук", "т"],
    )
    @Default("1")
    var pageNumber: Long
)