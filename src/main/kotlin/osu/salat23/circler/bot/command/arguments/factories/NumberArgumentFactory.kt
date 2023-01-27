package osu.salat23.circler.bot.command.arguments.factories

import osu.salat23.circler.bot.command.exceptions.CouldNotParseArgumentValueException
import kotlin.reflect.jvm.jvmName

abstract class NumberArgumentFactory: osu.salat23.circler.bot.command.arguments.factories.ArgumentFactory<osu.salat23.circler.bot.command.arguments.NumberArgument>() {
    override fun create(input: String, implicit: Boolean): osu.salat23.circler.bot.command.arguments.ProvidedArgument<osu.salat23.circler.bot.command.arguments.NumberArgument> {
        val identifiers = getConfiguredArgument().identifiers
        val prefix = '-'
        identifiers.forEach { identifier ->
            val stringToMatch = "$prefix$identifier "
            if (input.contains(stringToMatch, true)) {
                var index = input.indexOf(stringToMatch, ignoreCase = true)
                index += stringToMatch.length - 1
                // todo IMPORTANT!!! fix indexes
                if (input[index+1].isWhitespace() && !input[index+2].isWhitespace()) {
                    val valueStartingIndex = index+2

                    var value = ""
                    for (character in input.substring(valueStartingIndex)) {

                        if (character.isWhitespace()) {
                            break
                        }

                        value+=character
                    }

                    val numberValue: Long
                    try {
                        numberValue = value.toLong()
                    } catch (exception: Exception) {
                        throw CouldNotParseArgumentValueException(value, Long::class.jvmName)
                    }
                    return osu.salat23.circler.bot.command.arguments.ProvidedArgument.of(
                        osu.salat23.circler.bot.command.arguments.NumberArgument(
                            numberValue
                        )
                    )
                }
            }
        }

        return osu.salat23.circler.bot.command.arguments.ProvidedArgument.empty()
    }
}