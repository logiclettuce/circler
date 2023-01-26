package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.factories.BeatmapIdArgumentFactory
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchBeatmapCommand
import osu.salat23.circler.configuration.domain.CommandConfiguration

@Component
class FetchBeatmapIdCommandFactory(
    commandConfiguration: CommandConfiguration,
    val beatmapIdCommandFactory: BeatmapIdArgumentFactory
): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "fetchBeatmap"
    override fun create(input: String): Command {
        val beatmapIdArgument = beatmapIdCommandFactory.create(input, true)

        return FetchBeatmapCommand(
            beatmapIdArgument = beatmapIdArgument
        )
    }
}