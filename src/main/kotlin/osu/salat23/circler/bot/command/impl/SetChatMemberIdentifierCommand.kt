package osu.salat23.circler.bot.command.impl

import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default
import osu.salat23.circler.osu.domain.Mode

@Command(
    name = "Set identifier",
    description = "Used for specifying your username in this chat.",
    identifiers = ["nickname", "nick", "тшслтфьу", "тшсл"]
)
class SetChatMemberIdentifierCommand(
    @Argument(
        name = "Player",
        description = "Specify player username.",
        identifiers = ["nick", "nickname", "тшсл", "тшслтфьу"],
        implicit = true
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
    var gameMode: Mode
)