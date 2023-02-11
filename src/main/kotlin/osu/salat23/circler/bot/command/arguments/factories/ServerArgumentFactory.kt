package osu.salat23.circler.bot.command.arguments.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.ProvidedArgument
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.exceptions.ArgumentIsNotDefinedException
import osu.salat23.circler.configuration.domain.Argument
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.api.osu.Server

@Component
class ServerArgumentFactory(
    commandConfiguration: CommandConfiguration
) : ArgumentFactory<ServerArgument>(), ArgumentCallProducer<ServerArgument> {

    companion object {
        private const val ARGUMENT_KEY = "server"
    }

    private final val configuredArgument = commandConfiguration.arguments[ARGUMENT_KEY]
        ?: throw ArgumentIsNotDefinedException(ARGUMENT_KEY)

    override fun create(
        input: String,
        implicit: Boolean
    ): ProvidedArgument<ServerArgument> {
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

            for (server in Server.values()) {
                for (identifier in server.identifiers) {
                    if (identifier == value)
                        return ProvidedArgument.of(
                            ServerArgument(
                                server
                            )
                        )
                }
            }
        }

        identifiers.forEach { identifier ->
            val stringToMatch = "$prefix$identifier "
            if (input.contains(stringToMatch, true)) {
                var index = input.indexOf(stringToMatch, ignoreCase = true)
                index += stringToMatch.length - 1

                if (input[index].isWhitespace() && !input[index+1].isWhitespace()) {
                    val valueStartingIndex = index + 1

                    var value = ""
                    for (character in input.substring(valueStartingIndex)) {
                        if (character.isWhitespace()) {
                            break
                        }

                        value += character
                    }

                    for (server in Server.values()) {
                        for (identifier in server.identifiers) {
                            if (identifier == value) return ProvidedArgument.of(
                                ServerArgument(server)
                            )
                        }
                    }
                }
            }
        }

        return ProvidedArgument.of(ServerArgument(Server.Bancho))
    }

    override fun getConfiguredArgument(): Argument {
        return configuredArgument
    }

    override fun produceCall(argument: ServerArgument): String {
        val server = argument.value.identifiers[0]
        return "-${getConfiguredArgument().identifiers[0]} $server"
    }
}