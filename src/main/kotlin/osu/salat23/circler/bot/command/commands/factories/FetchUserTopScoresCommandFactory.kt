package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.NumberArgument
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchUserTopScoresCommand
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.command.arguments.factories.*

@Component
class FetchUserTopScoresCommandFactory(
    commandConfiguration: CommandConfiguration,
    private val actorArgumentFactory: ActorArgumentFactory,
    private val serverArgumentFactory: ServerArgumentFactory,
    private val pageSizeArgumentFactory: PageSizeArgumentFactory,
    private val pageNumberArgumentFactory: PageNumberArgumentFactory,
    private val gameModeArgumentFactory: GameModeArgumentFactory
): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "fetchUserTopScores"
    override fun create(input: String): Command {
        val actorArgument = actorArgumentFactory.create(input, true)
        val serverArgument = serverArgumentFactory.create(input).withDefault(ServerArgument(Server.Bancho))
        val pageSizeArgument = pageSizeArgumentFactory.create(input).withDefault(NumberArgument(5L))
        val pageNumberArgument = pageNumberArgumentFactory.create(input).withDefault(NumberArgument(1L))
        val gameModeArgument = gameModeArgumentFactory.create(input)

        return FetchUserTopScoresCommand(
            serverArgument = serverArgument,
            actorArgument = actorArgument,
            pageSizeArgument = pageSizeArgument,
            pageArgument = pageNumberArgument,
            gameModeArgument = gameModeArgument
        )
    }
}