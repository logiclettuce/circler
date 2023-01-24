package osu.salat23.circler.bot.command.commands

import osu.salat23.circler.bot.command.arguments.ProvidedArgument
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.arguments.StringArgument

class MapChatLeaderboardCommand(
    val serverArgument: ProvidedArgument<ServerArgument>,
    val beatmapIdArgument: ProvidedArgument<StringArgument>
): Command() {
}