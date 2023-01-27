package osu.salat23.circler.bot.command.commands

import osu.salat23.circler.bot.command.arguments.GameModeArgument
import osu.salat23.circler.bot.command.arguments.ProvidedArgument
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.arguments.StringArgument

class SetChatMemberIdentifierCommand(
    val actorArgument: ProvidedArgument<StringArgument>,
    val serverArgument: ProvidedArgument<ServerArgument>,
    val gameModeArgument: ProvidedArgument<GameModeArgument>
): Command()