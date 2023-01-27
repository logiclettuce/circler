package osu.salat23.circler.bot.command.commands

import org.aspectj.apache.bcel.classfile.Module.Provide
import osu.salat23.circler.bot.command.arguments.*

class MapChatLeaderboardCommand(
    val serverArgument: ProvidedArgument<ServerArgument>,
    val beatmapIdArgument: ProvidedArgument<BeatmapIdArgument>,
    val gameModeArgument: ProvidedArgument<GameModeArgument>,
    val modsArgument: ProvidedArgument<ModsArgument>
): Command() {
}