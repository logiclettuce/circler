package osu.salat23.circler.bot.command.impl

import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default
import osu.salat23.circler.osu.domain.Mode

@Command(
    name = "Beatmap leaderboard in chat",
    description = "Fetch leaderboard on specified beatmap in current chat.",
    identifiers = ["bleaderboard", "bl", "идуфвукищфкв", "ил"]
)
class MapChatLeaderboardCommand(
    @Argument(
        name = "Beatmap id",
        description = "Specify beatmap id. You can use beatmap link instead of plain id.",
        identifiers = ["beatmap", "bmp", "b", "иуфеьфз", "иьз", "и"],
        implicit = true
    )
    var beatmapId: String,

    @Argument(
        name = "Server",
        description = "Specify game server.",
        identifiers = ["server", "s", "ыукмук", "ы"],
    )
    @Default("bancho")
    var server: Server,

    @Argument(
        name = "Mode",
        description = "Specify game mode.",
        identifiers = ["mode", "m", "ьщву", "ь"],
    )
    @Default("default")
    var gameMode: Mode,

    @Argument(
        name = "Mods",
        description = "Specify mods. Example: HDDTHR",
        identifiers = ["mods", "md", "ьщвы", "ьв"]
    )
    var mods: String
)