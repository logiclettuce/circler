package osu.salat23.circler.bot.command.commands

import osu.salat23.circler.bot.command.arguments.BeatmapIdArgument
import osu.salat23.circler.bot.command.arguments.ProvidedArgument

class FetchBeatmapCommand(
    val beatmapIdArgument: ProvidedArgument<BeatmapIdArgument>,
): Command()