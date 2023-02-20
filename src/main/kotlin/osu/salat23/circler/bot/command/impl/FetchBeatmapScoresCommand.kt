package osu.salat23.circler.bot.command.impl

import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default
import osu.salat23.circler.osu.domain.Mode

@Command(
    name = "Beatmap scores",
    description = "Get list of scores on the specified beatmap.",
    identifiers = ["score", "scores", "s", "ысщку", "ысщкуы", "ы"]
)
class FetchBeatmapScoresCommand(
    @Argument(
        name = "Beatmap id",
        description = "Specify beatmap id. You can use beatmap link instead of plain id.",
        identifiers = ["beatmap", "bmp", "b", "иуфеьфз", "иьз", "и"],
        required = true,
    )
    var beatmapId: String,

    @Argument(
        name = "Player",
        description = "Specify player username.",
        identifiers = ["nick", "nickname", "тшсл", "тшслтфьу"],
    )
    @Default("")
    var actor: String,

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
        name = "Page size",
        description = "Specify amount of elements that result can contain at once.",
        identifiers = ["page", "p", "зфпу", "з"],
    )
    @Default("1")
    var pageSize: Int,

    @Argument(
        name = "Page number",
        description = "Specify the number of page. Used to cycle through large result lists. Starts at 1.",
        identifiers = ["number", "n", "тгьиук", "т"],
    )
    @Default("1")
    var pageNumber: Int,

    @Argument(
        name = "Mods",
        description = "Specify mods. Example: HDDTHR",
        identifiers = ["mods", "md", "ьщвы", "ьв"]
    )
    @Default("")
    var mods: String
)