package osu.salat23.circler.bot.commands.arguments.factories

import osu.salat23.circler.bot.commands.arguments.ActorArgument
import osu.salat23.circler.bot.commands.arguments.ProvidedArgument
import osu.salat23.circler.bot.commands.arguments.StringArgument
import osu.salat23.circler.configuration.domain.Argument

abstract class StringArgumentFactory<T: StringArgument>: ArgumentFactory<T>() {
    override fun create(input: String, implicit: Boolean): ProvidedArgument<T> {
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
            return ProvidedArgument.of(StringArgument(value))
        }

        identifiers.forEach { identifier ->
            val stringToMatch = "$prefix$identifier "
            if (input.contains(stringToMatch, true)) {
                var index = input.indexOf(stringToMatch, ignoreCase = true)
                index += stringToMatch.length - 1

                if (input[index+1].isWhitespace() && !input[index+2].isWhitespace()) {
                    val valueStartingIndex = index+2

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
                            value+=character
                            continue
                        }

                        if (character.isWhitespace()) {
                            break
                        }

                        value+=character
                    }

                    return ProvidedArgument.of(StringArgument(value))
                }
            }
        }

        return ProvidedArgument.empty()
    }
}