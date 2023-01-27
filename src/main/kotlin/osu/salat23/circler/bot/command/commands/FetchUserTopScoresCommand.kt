package osu.salat23.circler.bot.command.commands

import osu.salat23.circler.bot.command.arguments.*

class FetchUserTopScoresCommand(
    val pageSizeArgument: ProvidedArgument<NumberArgument>,
    val pageArgument: ProvidedArgument<NumberArgument>,
    val actorArgument: ProvidedArgument<StringArgument>,
    val serverArgument: ProvidedArgument<ServerArgument>,
    val gameModeArgument: ProvidedArgument<GameModeArgument>
): Command()