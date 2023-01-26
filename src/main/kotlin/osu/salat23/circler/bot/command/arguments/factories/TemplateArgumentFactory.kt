package osu.salat23.circler.bot.command.arguments.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.exceptions.ArgumentIsNotDefinedException
import osu.salat23.circler.configuration.domain.Argument
import osu.salat23.circler.configuration.domain.CommandConfiguration

@Component
class TemplateArgumentFactory(
    commandConfiguration: CommandConfiguration
): StringArgumentFactory() {
    companion object {
        private const val ARGUMENT_KEY = "template"
    }

    private final val configuredArgument = commandConfiguration.arguments[ARGUMENT_KEY]
        ?: throw ArgumentIsNotDefinedException(ARGUMENT_KEY)

    override fun getConfiguredArgument(): Argument {
        return configuredArgument
    }
}