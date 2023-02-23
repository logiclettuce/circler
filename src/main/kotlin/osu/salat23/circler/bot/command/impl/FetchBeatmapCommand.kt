package osu.salat23.circler.bot.command.impl

import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default
import osu.salat23.circler.osu.domain.Mode

@Command(
    name = "Fetch beatmap",
    description = "Get information about particular beatmap",
    identifiers = ["beatmap", "b"]
)
class FetchBeatmapCommand(
    @Argument(
        name = "Beatmap Id",
        description = "Specify beatmap id. Url with the beatmap can be used, as well as plain id.",
        identifiers = ["beatmap", "bmp"],
        required = true,
        implicit = true
    )
    @Default("")
    var beatmapId: String,

    @Argument(
        name = "Game mode",
        description = "Specify game mode.",
        identifiers = ["gamemode", "mode"]
    )
    @Default("default")
    var gameMode: Mode,

    @Argument(
        name = "Mods",
        description = "Specify mods. Example: HDDTHR",
        identifiers = ["mods", "md"]
    )
    @Default("")
    var mods: String
)