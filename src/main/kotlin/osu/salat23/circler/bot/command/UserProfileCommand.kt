package osu.salat23.circler.bot.command

import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default

@Command(
    name = "User profile",
    description = "Fetches user profile",
    identifiers = ["user", "u", "гыук", "г"]
)
class UserProfileCommand (
    @Argument(
        name = "Player",
        description = "Something written here",
        identifiers = ["nick", "nickname", "тшсл", "тшслтфьу"],
        required = true
    )
    val actor: String,

    @Argument(
        name = "Server",
        description = "Something written here",
        identifiers = ["server", "s", "ыукмук", "ы"],
    )
    @Default("bancho")
    val server: String,

    @Argument(
        name = "Mode",
        description = "Something written here",
        identifiers = ["mode", "m", "ьщву", "ь"],
    )
    @Default("standard")
    val mode: String
)