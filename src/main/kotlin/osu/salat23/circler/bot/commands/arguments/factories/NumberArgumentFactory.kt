package osu.salat23.circler.bot.commands.arguments.factories

import osu.salat23.circler.bot.commands.arguments.NumberArgument
import osu.salat23.circler.bot.commands.arguments.ProvidedArgument
import osu.salat23.circler.bot.commands.arguments.StringArgument
import osu.salat23.circler.bot.commands.exceptions.CouldNotParseArgumentValueException
import osu.salat23.circler.configuration.domain.Argument
import kotlin.reflect.jvm.jvmName

abstract class NumberArgumentFactory<T: NumberArgument>: ArgumentFactory<T>() {
    override fun create(input: String, implicit: Boolean): ProvidedArgument<T> {
        val identifiers = getConfiguredArgument().identifiers
        val prefix = '-'
        identifiers.forEach { identifier ->
            val stringToMatch = "$prefix$identifier "
            if (input.contains(stringToMatch, true)) {
                var index = input.indexOf(stringToMatch, ignoreCase = true)
                index += stringToMatch.length - 1

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
                    return ProvidedArgument.of(NumberArgument(numberValue))
                }
            }
        }

        return ProvidedArgument.empty()
    }
}