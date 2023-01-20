package osu.salat23.circler.bot.command.arguments.factories

import osu.salat23.circler.bot.command.arguments.Argument
import osu.salat23.circler.configuration.domain.Argument as ConfigArgument
import osu.salat23.circler.bot.command.arguments.ProvidedArgument

abstract class ArgumentFactory<T: osu.salat23.circler.bot.command.arguments.Argument> {
    abstract fun create(input: String, implicit: Boolean = false): osu.salat23.circler.bot.command.arguments.ProvidedArgument<T>
    protected abstract fun getConfiguredArgument(): ConfigArgument

}