package osu.salat23.circler.bot.command.arguments.factories

import osu.salat23.circler.bot.command.arguments.ProvidedArgument
import osu.salat23.circler.bot.command.arguments.StringArgument

abstract class StringArgumentFactory: ArgumentFactory<StringArgument>() {
    override fun create(input: String, implicit: Boolean): ProvidedArgument<StringArgument> {
        val identifiers = getConfiguredArgument().identifiers
        val prefix = '-'
        // todo "u n duckpicker" gives exception because it thinks 'n' is an implicit nickname. maybe check for excess tokens???
        if (implicit && input.isNotBlank() && !input.startsWith(prefix)) {
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
            return ProvidedArgument.of(
                StringArgument(
                    value
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

                    return ProvidedArgument.of(
                        StringArgument(
                            value
                        )
                    )
                }
            }
        }

        return ProvidedArgument.empty()
    }
}