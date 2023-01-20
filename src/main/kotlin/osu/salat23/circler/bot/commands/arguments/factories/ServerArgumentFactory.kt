package osu.salat23.circler.bot.commands.arguments.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.commands.arguments.ProvidedArgument
import osu.salat23.circler.bot.commands.arguments.ServerArgument
import osu.salat23.circler.bot.commands.arguments.StringArgument
import osu.salat23.circler.bot.commands.exceptions.ArgumentIsNotDefinedException
import osu.salat23.circler.configuration.domain.Argument
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.osu.Server

@Component
class ServerArgumentFactory(
    commandConfiguration: CommandConfiguration
): ArgumentFactory<ServerArgument>() {

    companion object {
        private const val ARGUMENT_KEY = "actor"
    }

    private final val configuredArgument = commandConfiguration.arguments[ARGUMENT_KEY]
        ?: throw ArgumentIsNotDefinedException(ARGUMENT_KEY)
    override fun create(input: String, implicit: Boolean): ProvidedArgument<ServerArgument> {
        val identifiers = getConfiguredArgument().identifiers
        val prefix = '-'

        if (implicit && input.isNotBlank()) {
            var value = ""
            var isQuoted = false
            for (character in input.trim()) {

                // opening quotes
                if (character == '"' && !isQuoted) {
                    isQuoted = true
                    continue
                }

                // closing quotes
                if (character == '"') {
                    break
                }

                if (isQuoted && character.isWhitespace()) {
                    value+=character
                    continue
                }

                if (character.isWhitespace()) {
                    break
                }

                value += character
            }

            for (server in Server.values()) {
                for (identifier in server.identifiers) {
                    if (identifier == value) return ProvidedArgument.of(ServerArgument(server))
                }
            }
        }

        identifiers.forEach { identifier ->
            val stringToMatch = "$prefix$identifier "
            if (input.contains(stringToMatch, true)) {
                var index = input.indexOf(stringToMatch, ignoreCase = true)
                index += stringToMatch.length - 1

                if (input[index + 1].isWhitespace() && !input[index + 2].isWhitespace()) {
                    val valueStartingIndex = index + 2

                    var value = ""
                    var isQuoted = false
                    for (character in input.substring(valueStartingIndex)) {

                        // opening quotes
                        if (character == '"' && !isQuoted) {
                            isQuoted = true
                            continue
                        }

                        // closing quotes
                        if (character == '"') {
                            break
                        }

                        if (isQuoted && character.isWhitespace()) {
                            value += character
                            continue
                        }

                        if (character.isWhitespace()) {
                            break
                        }

                        value += character
                    }

                    for (server in Server.values()) {
                        for (identifier in server.identifiers) {
                            if (identifier == value) return ProvidedArgument.of(ServerArgument(server))
                        }
                    }
                }
            }
        }

        return ProvidedArgument.empty()
    }

    override fun getConfiguredArgument(): Argument {
        return configuredArgument
    }
}