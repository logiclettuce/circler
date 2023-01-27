package osu.salat23.circler.bot.command.arguments.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.ModsArgument
import osu.salat23.circler.bot.command.arguments.ProvidedArgument
import osu.salat23.circler.bot.command.exceptions.ArgumentIsNotDefinedException
import osu.salat23.circler.configuration.domain.Argument
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.osu.domain.Mod
import osu.salat23.circler.osu.domain.ModsFilterType

@Component
class ModsArgumentFactory(
    commandConfiguration: CommandConfiguration
): ArgumentFactory<ModsArgument>() {
    companion object {
        private const val ARGUMENT_KEY = "mods"
    }

    private final val configuredArgument = commandConfiguration.arguments[ARGUMENT_KEY]
        ?: throw ArgumentIsNotDefinedException(ARGUMENT_KEY)
    override fun create(input: String, implicit: Boolean): ProvidedArgument<ModsArgument> {
        val identifiers = getConfiguredArgument().identifiers

        val prefix = "-"

        if (implicit && input.isNotBlank() && !input.startsWith(prefix)) {
            var value = ""
            for (character in input.trim()) {
                if (character.isWhitespace()) {
                    break
                }

                value += character
            }

            // decide mod filter type based on prefix. Inclusive mode by default.
            // remove prefix afterwards (if it exists)
            var modFilterType: ModsFilterType? = null
            ModsFilterType.values().forEach { if (value.startsWith(it.prefix)) modFilterType = it }
            if (modFilterType != null) value = value.substring(1)
            else modFilterType = ModsFilterType.Inclusive

            if (value.length < 2) return ProvidedArgument.of(ModsArgument(mods = emptyList(), filterType = modFilterType!!))
            val modNames = value.chunked(2)
            val modNameToMod =  Mod.values().associateBy { it.alternativeName }
            val mods = mutableListOf<Mod>()

            modNames.forEach{ modName ->
                if (modName in modNameToMod) mods.add(modNameToMod[modName]!!)
            }

            ProvidedArgument.of(ModsArgument(mods = mods, filterType = modFilterType!!))
        }

        identifiers.forEach { identifier ->
            val stringToMatch = "$prefix$identifier "
            if (input.contains(stringToMatch, true)) {
                var index = input.indexOf(stringToMatch, ignoreCase = true)
                index += stringToMatch.length - 1

                if (input[index].isWhitespace() && !input[index + 1].isWhitespace()) {
                    val valueStartingIndex = index + 1

                    var value = ""
                    for (character in input.substring(valueStartingIndex)) {
                        if (character.isWhitespace()) {
                            break
                        }

                        value += character
                    }

                    // decide mod filter type based on prefix. Inclusive mode by default.
                    // remove prefix afterwards (if it exists)
                    var modFilterType: ModsFilterType? = null
                    ModsFilterType.values().forEach { if (value.startsWith(it.prefix)) modFilterType = it }
                    if (modFilterType != null) value = value.substring(1)
                    else modFilterType = ModsFilterType.Inclusive

                    if (value.length < 2) return ProvidedArgument.of(ModsArgument(mods = emptyList(), filterType = modFilterType!!))
                    val modNames = value.chunked(2)
                    val modNameToMod =  Mod.values().associateBy { it.alternativeName }
                    val mods = mutableListOf<Mod>()

                    modNames.forEach{ modName ->
                        if (modName in modNameToMod) mods.add(modNameToMod[modName]!!)
                    }

                    ProvidedArgument.of(ModsArgument(mods = mods, filterType = modFilterType!!))
                }
            }
        }

        return ProvidedArgument.of(ModsArgument(mods = emptyList(), filterType = ModsFilterType.Inclusive))
    }

    override fun getConfiguredArgument(): Argument = configuredArgument

}