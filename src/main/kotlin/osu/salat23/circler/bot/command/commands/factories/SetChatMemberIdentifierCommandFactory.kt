package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.arguments.factories.ActorArgumentFactory
import osu.salat23.circler.bot.command.arguments.factories.ServerArgumentFactory
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.SetChatMemberIdentifierCommand
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.command.arguments.factories.GameModeArgumentFactory

@Component
class SetChatMemberIdentifierCommandFactory(
    commandConfiguration: CommandConfiguration,
    private val actorArgumentFactory: ActorArgumentFactory,
    private val serverArgumentFactory: ServerArgumentFactory,
    private val gameModeArgumentFactory: GameModeArgumentFactory
): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "setChatMemberIdentifier"
    override fun create(input: String): Command {
        val actorArgument = actorArgumentFactory.create(input, true)
        val serverArgument = serverArgumentFactory.create(input).withDefault(ServerArgument(Server.Bancho))
        val gameModeArgument = gameModeArgumentFactory.create(input)

        return SetChatMemberIdentifierCommand(
            actorArgument = actorArgument,
            serverArgument = serverArgument,
            gameModeArgument = gameModeArgument
        )
    }
}