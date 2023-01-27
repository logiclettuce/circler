package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.arguments.factories.BeatmapIdArgumentFactory
import osu.salat23.circler.bot.command.arguments.factories.ServerArgumentFactory
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.MapChatLeaderboardCommand
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.command.arguments.factories.GameModeArgumentFactory
import osu.salat23.circler.bot.command.arguments.factories.ModsArgumentFactory

@Component
class MapChatLeaderboardCommandFactory(
    commandConfiguration: CommandConfiguration,
    private val beatmapIdArgumentFactory: BeatmapIdArgumentFactory,
    private val serverArgumentFactory: ServerArgumentFactory,
    private val gameModeArgumentFactory: GameModeArgumentFactory,
    private val modsArgumentFactory: ModsArgumentFactory
): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "chatMapLeaderboard"
    override fun create(input: String): Command {
        val beatmapIdArgument = beatmapIdArgumentFactory.create(input, true)
        val serverArgument = serverArgumentFactory.create(input).withDefault(ServerArgument(Server.Bancho))
        val gameModeArgument = gameModeArgumentFactory.create(input)
        val modsArgument = modsArgumentFactory.create(input)

        return MapChatLeaderboardCommand(
            beatmapIdArgument = beatmapIdArgument,
            serverArgument = serverArgument,
            gameModeArgument = gameModeArgument,
            modsArgument = modsArgument
        )
    }
}