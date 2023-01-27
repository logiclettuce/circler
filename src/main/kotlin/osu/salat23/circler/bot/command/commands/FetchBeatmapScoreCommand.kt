package osu.salat23.circler.bot.command.commands

import osu.salat23.circler.bot.command.arguments.*

class FetchBeatmapScoreCommand(
    val actorArgument: ProvidedArgument<StringArgument>,
    val serverArgument: ProvidedArgument<ServerArgument>,
    val beatmapIdArgument: ProvidedArgument<BeatmapIdArgument>,
    val pageSizeArgument: ProvidedArgument<NumberArgument>,
    val pageNumberArgument: ProvidedArgument<NumberArgument>
): Command()