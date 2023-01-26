package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.arguments.factories.ServerArgumentFactory
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchUserProfileCommand
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.osu.Server

@Component
class FetchUserProfileCommandFactory(
    commandConfiguration: CommandConfiguration,
    private val actorArgumentFactory: osu.salat23.circler.bot.command.arguments.factories.ActorArgumentFactory,
    val serverArgumentFactory: ServerArgumentFactory
): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "fetchUserProfile"
    override fun create(input: String): Command {
        val actorArgument = actorArgumentFactory.create(input, true)
        val serverArgument = serverArgumentFactory.create(input).withDefault(
            ServerArgument(
                Server.Bancho
            )
        )

        return FetchUserProfileCommand(
            actorArgument = actorArgument,
            serverArgument = serverArgument
        )

    }
}