package osu.salat23.circler.bot.command.commands

import osu.salat23.circler.bot.command.annotations.Argument
import osu.salat23.circler.bot.command.annotations.Command
import osu.salat23.circler.bot.command.annotations.Default
import osu.salat23.circler.bot.command.arguments.BeatmapIdArgument
import osu.salat23.circler.bot.command.arguments.GameModeArgument
import osu.salat23.circler.bot.command.arguments.ModsArgument
import osu.salat23.circler.bot.command.arguments.ProvidedArgument

@Command(
    name = "Fetch beatmap",
    description = "Get information about particular beatmap",
    identifiers = ["beatmap", "b", "иуфеьфз", "и"]
)
class FetchBeatmapCommand(
    @Argument(
        name = "Beatmap Id",
        description = "Specify beatmap id. Url with the beatmap can be used, as well as plain id.",
        identifiers = ["beatmap", "bmp", "иуфеьфз", "иьз"],
        implicit = true,
        required = true
    )
    val beatmapId: String,

    @Argument(
        name = "Game mode",
        description = "Specify game mode.",
        identifiers = ["gamemode", "mode", "пфьуьщву", "ьщву"]
    )
    @Default("default")
    val gameMode: String,
    val mods: String
)