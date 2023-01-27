package osu.salat23.circler.bot.command.arguments

import osu.salat23.circler.api.osu.Server

class BeatmapIdArgument(
    val id: String,
    val server: Server
): Argument()