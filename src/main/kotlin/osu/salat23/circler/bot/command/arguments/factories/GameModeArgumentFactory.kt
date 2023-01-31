package osu.salat23.circler.bot.command.arguments.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.GameModeArgument
import osu.salat23.circler.bot.command.arguments.ProvidedArgument
import osu.salat23.circler.bot.command.exceptions.ArgumentIsNotDefinedException
import osu.salat23.circler.configuration.domain.Argument
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.osu.domain.Mode

@Component
class GameModeArgumentFactory(
    commandConfiguration: CommandConfiguration
) : ArgumentFactory<GameModeArgument>() {
    companion object {
        private const val ARGUMENT_KEY = "gameMode"
    }

    private final val configuredArgument = commandConfiguration.arguments[ARGUMENT_KEY]
        ?: throw ArgumentIsNotDefinedException(ARGUMENT_KEY)

    override fun create(input: String, implicit: Boolean): ProvidedArgument<GameModeArgument> {
        val identifiers = getConfiguredArgument().identifiers
        val prefix = '-'

        if (implicit && input.isNotBlank() && !input.startsWith(prefix)) {
            var value = ""
            for (character in input.trim()) {
                if (character.isWhitespace()) {
                    break
                }

                value += character
            }

            for (mode in Mode.values()) {
                for (identifier in mode.identifiers) {
                    if (identifier == value) return ProvidedArgument.of(
                        GameModeArgument(mode)
                    )
                }
            }
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

                    for (mode in Mode.values()) {
                        for (identifier in mode.identifiers) {
                            if (identifier == value) return ProvidedArgument.of(
                                GameModeArgument(mode)
                            )
                        }
                    }
                }
            }
        }

        return ProvidedArgument.of(GameModeArgument(Mode.Standard))
    }

    override fun getConfiguredArgument(): Argument {
        return configuredArgument
    }
}