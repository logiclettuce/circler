package osu.salat23.circler.bot.command.commands

import osu.salat23.circler.bot.command.arguments.GameModeArgument
import osu.salat23.circler.bot.command.arguments.ProvidedArgument
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.arguments.StringArgument

class FetchUserProfileCommand(
    val actorArgument: ProvidedArgument<StringArgument>,
    val gameModeArgument: ProvidedArgument<GameModeArgument>,
    val serverArgument: ProvidedArgument<ServerArgument>
) : Command() {


}