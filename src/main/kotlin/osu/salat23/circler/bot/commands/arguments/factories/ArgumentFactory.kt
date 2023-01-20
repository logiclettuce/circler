package osu.salat23.circler.bot.commands.arguments.factories

import osu.salat23.circler.bot.commands.arguments.Argument
import osu.salat23.circler.configuration.domain.Argument as ConfigArgument
import osu.salat23.circler.bot.commands.arguments.ProvidedArgument

abstract class ArgumentFactory<T: Argument> {
    abstract fun create(input: String, implicit: Boolean = false): ProvidedArgument<T>
    protected abstract fun getConfiguredArgument(): ConfigArgument

}