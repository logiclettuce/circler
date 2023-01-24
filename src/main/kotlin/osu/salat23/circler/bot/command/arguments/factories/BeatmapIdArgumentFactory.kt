package osu.salat23.circler.bot.command.arguments.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.BeatmapIdArgument
import osu.salat23.circler.bot.command.arguments.ProvidedArgument
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.exceptions.ArgumentIsNotDefinedException
import osu.salat23.circler.configuration.domain.Argument
import osu.salat23.circler.configuration.domain.CommandConfiguration

@Component
class BeatmapIdArgumentFactory(
    commandConfiguration: CommandConfiguration
):ArgumentFactory<BeatmapIdArgument>()  {
    companion object {
        private const val ARGUMENT_KEY = "beatmapId"
    }

    private final val configuredArgument = commandConfiguration.arguments[ARGUMENT_KEY]
        ?: throw ArgumentIsNotDefinedException(ARGUMENT_KEY)

    override fun create(input: String, implicit: Boolean): ProvidedArgument<BeatmapIdArgument> {

    }

    override fun getConfiguredArgument(): Argument {
        return configuredArgument
    }

}