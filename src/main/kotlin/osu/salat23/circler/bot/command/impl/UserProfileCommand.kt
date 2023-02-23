package osu.salat23.circler.bot.command.impl

import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default
import osu.salat23.circler.osu.domain.Mode

@Command(
    name = "User profile",
    description = "Fetches user profile",
    identifiers = ["user", "u"]
)
class UserProfileCommand (
    @Argument(
        name = "Player",
        description = "Specify player username.",
        identifiers = ["nick", "nickname", "n"],
        implicit = true
    )
    @Default("")
    var actor: String,

    @Argument(
        name = "Server",
        description = "Specify game server.",
        identifiers = ["server", "s"],
    )
    @Default("bancho")
    var server: Server,

    @Argument(
        name = "Mode",
        description = "Specify game mode.",
        identifiers = ["mode", "m"],
    )
    @Default("default")
    var gameMode: Mode,

    @Argument(
        name = "Render mode",
        description = "Enables render mode",
        identifiers = ["render", "r"]
    )
    @Default("false")
    var isRenderMode: Boolean
)