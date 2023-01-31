package osu.salat23.circler.bot.command.arguments.factories

import osu.salat23.circler.bot.command.arguments.BooleanArgument
import osu.salat23.circler.bot.command.arguments.ProvidedArgument

abstract class BooleanArgumentFactory : ArgumentFactory<BooleanArgument>() {
    override fun create(input: String, implicit: Boolean): ProvidedArgument<BooleanArgument> {
        val identifiers = getConfiguredArgument().identifiers
        val prefix = '-' // todo move prefix to a generic place

        if (implicit && input.isNotBlank() && !input.startsWith(prefix)) {
            var value = ""
            for (character in input.trim()) {
                if (character.isWhitespace()) {
                    break
                }
                value += character
            }
            // todo make private functions in different factories which will handle processing of value
            val boolean = value == "true" || value == "t" || value == "1"
            return ProvidedArgument.of(
                BooleanArgument(boolean)
            )
        }

        identifiers.forEach { identifier ->
            val stringToMatch = "$prefix$identifier"
            if (input.contains(stringToMatch, true)) {
                var index = input.indexOf(stringToMatch, ignoreCase = true)
                index += stringToMatch.length - 1 + 1

                var value = ""
                for (character in input.substring(index).trim()) {
                    if (character.isWhitespace()) {
                        break
                    }
                    value += character
                }
                val boolean = value == "true" || value == "t" || value == "1" || value.isBlank()
                return ProvidedArgument.of(
                    BooleanArgument(boolean)
                )
            }
        }

        return ProvidedArgument.of(BooleanArgument(false))
    }
}