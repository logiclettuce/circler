package osu.salat23.circler.bot.command.commands

import osu.salat23.circler.bot.command.arguments.ProvidedArgument
import osu.salat23.circler.bot.command.arguments.ServerArgument

class ChatLeaderboardCommand(
    val serverArgument: ProvidedArgument<ServerArgument>
): Command()