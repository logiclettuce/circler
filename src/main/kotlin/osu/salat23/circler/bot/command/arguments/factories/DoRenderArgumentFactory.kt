package osu.salat23.circler.bot.command.arguments.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.BooleanArgument
import osu.salat23.circler.bot.command.exceptions.ArgumentIsNotDefinedException
import osu.salat23.circler.configuration.domain.Argument
import osu.salat23.circler.configuration.domain.CommandConfiguration

@Component
class DoRenderArgumentFactory(
    private final val commandConfiguration: CommandConfiguration
): BooleanArgumentFactory(), ArgumentCallProducer<BooleanArgument> {
    companion object {
        private const val ARGUMENT_KEY = "doRender"
    }

    private final val configuredArgument = commandConfiguration.arguments[ARGUMENT_KEY]
        ?: throw ArgumentIsNotDefinedException(ARGUMENT_KEY)

    override fun getConfiguredArgument(): Argument {
        return configuredArgument
    }

    override fun produceCall(argument: BooleanArgument): String {
        val doRender = argument.value
        var res = ""
        if (doRender) res += "-${getConfiguredArgument().identifiers[0]}"
        return res
    }
}