package osu.salat23.circler.bot.command.impl

import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default

@Command(
    name = "Help",
    description = "Get in-depth information about available commands and their arguments.",
    identifiers = ["help", "h"]
)
class HelpCommand(

    @Argument(
        name = "Command",
        description = "Specify command.",
        identifiers = ["command", "c"],
        implicit = true
    )
    @Default("")
    val commandIdentifier: String
)