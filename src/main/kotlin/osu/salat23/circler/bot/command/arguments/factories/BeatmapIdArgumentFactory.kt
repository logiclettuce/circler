package osu.salat23.circler.bot.command.arguments.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.BeatmapIdArgument
import osu.salat23.circler.bot.command.arguments.ProvidedArgument
import osu.salat23.circler.bot.command.exceptions.ArgumentIsNotDefinedException
import osu.salat23.circler.configuration.domain.Argument
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.api.osu.Server

@Component
class BeatmapIdArgumentFactory(
    commandConfiguration: CommandConfiguration
):ArgumentFactory<BeatmapIdArgument>()  {
    companion object {
        private const val ARGUMENT_KEY = "beatmapId"
    }

    private final val configuredArgument = commandConfiguration.arguments[ARGUMENT_KEY]
        ?: throw ArgumentIsNotDefinedException(ARGUMENT_KEY)

    override fun create(input: String, implicit: Boolean): ProvidedArgument<BeatmapIdArgument> {
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
                if (server.beatmapsetUrl.matches(value)) {
                    var beatmapId = ""
                    for (beatmapUrlIndex in value.length-1 downTo 0) {
                        if (value[beatmapUrlIndex] == '/') break
                        beatmapId += value[beatmapUrlIndex]
                    }
                    beatmapId = beatmapId.reversed()

                    return ProvidedArgument.of(
                        BeatmapIdArgument(
                            id = beatmapId,
                            server = server
                        )
                    )
                }
            }
            if (value.matches(Regex("\\d+")))
                return ProvidedArgument.of(
                    BeatmapIdArgument(
                        id = value,
                        server = Server.Bancho
                    )
                )
        }

        identifiers.forEach { identifier ->
            val stringToMatch = "$prefix$identifier "
            if (input.contains(stringToMatch, true)) {
                var index = input.indexOf(stringToMatch, ignoreCase = true)
                index += stringToMatch.length - 1

                if (input[index].isWhitespace() && !input[index+1].isWhitespace()) {
                    val valueStartingIndex = index+1

                    var value = ""
                    for (character in input.substring(valueStartingIndex)) {

                        if (character.isWhitespace()) {
                            value+=character
                            continue
                        }

                        if (character.isWhitespace()) {
                            break
                        }

                        value+=character
                    }

                    for (server in Server.values()) {
                        if (server.beatmapsetUrl.matches(value)) {
                            var beatmapId = ""
                            for (beatmapUrlIndex in value.length-1 downTo 0) {
                                if (value[beatmapUrlIndex] == '/') break
                                beatmapId += value[beatmapUrlIndex]
                            }
                            beatmapId = beatmapId.reversed()

                            return ProvidedArgument.of(
                                BeatmapIdArgument(
                                    id = beatmapId,
                                    server = server
                                )
                            )
                        }
                    }
                    if (value.matches(Regex("\\d+")))
                        return ProvidedArgument.of(
                            BeatmapIdArgument(
                                id = value,
                                server = Server.Bancho
                            )
                        )
                }
            }
        }

        return ProvidedArgument.empty()
    }

    override fun getConfiguredArgument(): Argument {
        return configuredArgument
    }

}