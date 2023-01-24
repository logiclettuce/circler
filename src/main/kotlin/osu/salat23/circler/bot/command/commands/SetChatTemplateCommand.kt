package osu.salat23.circler.bot.command.commands

import osu.salat23.circler.bot.command.arguments.ProvidedArgument
import osu.salat23.circler.bot.command.arguments.StringArgument

class SetChatTemplateCommand(
    val templateArgument: ProvidedArgument<StringArgument>
): Command()