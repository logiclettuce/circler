package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.factories.BeatmapIdArgumentFactory
import osu.salat23.circler.bot.command.arguments.factories.GameModeArgumentFactory
import osu.salat23.circler.bot.command.arguments.factories.ModsArgumentFactory
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchBeatmapCommand
import osu.salat23.circler.configuration.domain.CommandConfiguration

@Component
class FetchBeatmapCommandFactory(
    commandConfiguration: CommandConfiguration,
    val beatmapIdCommandFactory: BeatmapIdArgumentFactory,
    val gameModeArgumentFactory: GameModeArgumentFactory,
    val modsArgumentFactory: ModsArgumentFactory
): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "fetchBeatmap"
    override fun create(input: String): Command {
        val beatmapIdArgument = beatmapIdCommandFactory.create(input, true)
        val gameModeArgument = gameModeArgumentFactory.create(input)
        val modsArgument = modsArgumentFactory.create(input)

        return FetchBeatmapCommand(
            beatmapIdArgument = beatmapIdArgument,
            gameModeArgument = gameModeArgument,
            modsArgument = modsArgument
        )
    }
}