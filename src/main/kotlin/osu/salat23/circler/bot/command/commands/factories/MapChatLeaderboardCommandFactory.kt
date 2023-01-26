package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.factories.BeatmapIdArgumentFactory
import osu.salat23.circler.bot.command.arguments.factories.ServerArgumentFactory
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.MapChatLeaderboardCommand
import osu.salat23.circler.configuration.domain.CommandConfiguration

@Component
class MapChatLeaderboardCommandFactory(
    commandConfiguration: CommandConfiguration,
    private val beatmapIdArgumentFactory: BeatmapIdArgumentFactory,
    private val serverArgumentFactory: ServerArgumentFactory
): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "chatMapLeaderboard"
    override fun create(input: String): Command {
        val beatmapIdArgument = beatmapIdArgumentFactory.create(input, true)
        val serverArgument = serverArgumentFactory.create(input)

        return MapChatLeaderboardCommand(
            beatmapIdArgument = beatmapIdArgument,
            serverArgument = serverArgument
        )
    }
}