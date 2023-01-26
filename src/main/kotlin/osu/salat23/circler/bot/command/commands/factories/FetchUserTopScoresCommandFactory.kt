package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.NumberArgument
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.arguments.factories.ActorArgumentFactory
import osu.salat23.circler.bot.command.arguments.factories.PageNumberArgumentFactory
import osu.salat23.circler.bot.command.arguments.factories.PageSizeArgumentFactory
import osu.salat23.circler.bot.command.arguments.factories.ServerArgumentFactory
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchUserTopScoresCommand
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.osu.Server

@Component
class FetchUserTopScoresCommandFactory(
    commandConfiguration: CommandConfiguration,
    private val actorArgumentFactory: ActorArgumentFactory,
    private val serverArgumentFactory: ServerArgumentFactory,
    private val pageSizeArgumentFactory: PageSizeArgumentFactory,
    private val pageNumberArgumentFactory: PageNumberArgumentFactory,
): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "fetchUserTopScores"
    override fun create(input: String): Command {
        val actorArgument = actorArgumentFactory.create(input, true)
        val serverArgument = serverArgumentFactory.create(input).withDefault(ServerArgument(Server.Bancho))
        val pageSizeArgument = pageSizeArgumentFactory.create(input).withDefault(NumberArgument(5L))
        val pageNumberArgument = pageNumberArgumentFactory.create(input).withDefault(NumberArgument(1L))

        return FetchUserTopScoresCommand(
            serverArgument = serverArgument,
            actorArgument = actorArgument,
            pageSizeArgument = pageSizeArgument,
            pageArgument = pageNumberArgument
        )
    }
}