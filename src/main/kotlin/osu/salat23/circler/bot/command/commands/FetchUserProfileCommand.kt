package osu.salat23.circler.bot.command.commands

import osu.salat23.circler.bot.command.arguments.*

class FetchUserProfileCommand(
    val actorArgument: ProvidedArgument<StringArgument>,
    val gameModeArgument: ProvidedArgument<GameModeArgument>,
    val serverArgument: ProvidedArgument<ServerArgument>,
    val isHtmlArgument: ProvidedArgument<BooleanArgument>
) : Command() {


}