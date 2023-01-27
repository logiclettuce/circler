package osu.salat23.circler.bot.command.commands

import osu.salat23.circler.bot.command.arguments.BeatmapIdArgument
import osu.salat23.circler.bot.command.arguments.GameModeArgument
import osu.salat23.circler.bot.command.arguments.ModsArgument
import osu.salat23.circler.bot.command.arguments.ProvidedArgument

class FetchBeatmapCommand(
    val beatmapIdArgument: ProvidedArgument<BeatmapIdArgument>,
    val gameModeArgument: ProvidedArgument<GameModeArgument>,
    val modsArgument: ProvidedArgument<ModsArgument>
): Command()