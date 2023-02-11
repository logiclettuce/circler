package osu.salat23.circler.bot.command.arguments.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.StringArgument
import osu.salat23.circler.bot.command.exceptions.ArgumentIsNotDefinedException
import osu.salat23.circler.configuration.domain.Argument
import osu.salat23.circler.configuration.domain.CommandConfiguration

@Component
class ActorArgumentFactory(
    commandConfiguration: CommandConfiguration
): StringArgumentFactory(), ArgumentCallProducer<StringArgument> {
    companion object {
        private const val ARGUMENT_KEY = "actor"
    }

    private final val configuredArgument = commandConfiguration.arguments[ARGUMENT_KEY]
        ?: throw ArgumentIsNotDefinedException(ARGUMENT_KEY)

    override fun getConfiguredArgument(): Argument {
        return configuredArgument
    }

    override fun produceCall(argument: StringArgument): String {
        val actor = argument.value
        return "-${getConfiguredArgument().identifiers[0]} $actor"
    }
}