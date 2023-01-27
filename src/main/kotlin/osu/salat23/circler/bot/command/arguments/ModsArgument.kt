package osu.salat23.circler.bot.command.arguments

import osu.salat23.circler.osu.domain.Mod
import osu.salat23.circler.osu.domain.ModsFilterType

class ModsArgument(
    val mods: List<Mod>,
    val filterType: ModsFilterType
): Argument()