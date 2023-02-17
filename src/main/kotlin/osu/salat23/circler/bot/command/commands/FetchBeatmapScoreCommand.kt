package osu.salat23.circler.bot.command.commands

import osu.salat23.circler.bot.command.arguments.*

class FetchBeatmapScoreCommand(
    val actorArgument: ProvidedArgument<StringArgument>,
    val serverArgument: ProvidedArgument<ServerArgument>,
    val beatmapIdArgument: ProvidedArgument<BeatmapIdArgument>,
    val gameModeArgument: ProvidedArgument<GameModeArgument>,
    val pageSizeArgument: ProvidedArgument<NumberArgument>,
    val pageNumberArgument: ProvidedArgument<NumberArgument>,
    val modsArgument: ProvidedArgument<ModsArgument>
): Command()