package osu.salat23.circler.bot.commands.arguments.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.commands.arguments.ActorArgument
import osu.salat23.circler.bot.commands.arguments.ProvidedArgument
import osu.salat23.circler.bot.commands.exceptions.ArgumentIsNotDefinedException
import osu.salat23.circler.configuration.domain.Argument
import osu.salat23.circler.configuration.domain.CommandConfiguration

@Component
class ActorArgumentFactory(
    commandConfiguration: CommandConfiguration
): StringArgumentFactory<ActorArgument>() {
    companion object {
        private const val ARGUMENT_KEY = "actor"
    }

    private final val configuredArgument = commandConfiguration.arguments[ARGUMENT_KEY]
        ?: throw ArgumentIsNotDefinedException(ARGUMENT_KEY)

    override fun getConfiguredArgument(): Argument {
        return configuredArgument
    }

}