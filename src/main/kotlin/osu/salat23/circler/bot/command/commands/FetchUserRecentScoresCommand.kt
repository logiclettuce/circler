package osu.salat23.circler.bot.command.commands

import osu.salat23.circler.bot.command.arguments.NumberArgument
import osu.salat23.circler.bot.command.arguments.ProvidedArgument
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.arguments.StringArgument

class FetchUserRecentScoresCommand(
    val pageSizeArgument: ProvidedArgument<NumberArgument>,
    val pageArgument: ProvidedArgument<NumberArgument>,
    val actorArgument: ProvidedArgument<StringArgument>,
    val serverArgument: ProvidedArgument<ServerArgument>
): Command()