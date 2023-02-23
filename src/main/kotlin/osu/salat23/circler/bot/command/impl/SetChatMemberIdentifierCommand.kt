package osu.salat23.circler.bot.command.impl

import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default
import osu.salat23.circler.osu.domain.Mode

@Command(
    name = "Set identifier",
    description = "Used for specifying your username in this chat.",
    identifiers = ["nickname", "nick", "n"]
)
class SetChatMemberIdentifierCommand(
    @Argument(
        name = "Player",
        description = "Specify player username.",
        identifiers = ["nick", "nickname"],
        required = true,
        implicit = true
    )
    var actor: String,

    @Argument(
        name = "Server",
        description = "Specify game server.",
        identifiers = ["server", "s"],
    )
    @Default("bancho")
    var server: Server,
)