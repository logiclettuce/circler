package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.arguments.factories.ServerArgumentFactory
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchUserProfileCommand
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.command.arguments.factories.GameModeArgumentFactory
import osu.salat23.circler.bot.command.arguments.factories.IsHtmlArgumentFactory

@Component
class FetchUserProfileCommandFactory(
    commandConfiguration: CommandConfiguration,
    private final val actorArgumentFactory: osu.salat23.circler.bot.command.arguments.factories.ActorArgumentFactory,
    private final val serverArgumentFactory: ServerArgumentFactory,
    private final val gameModeArgumentFactory: GameModeArgumentFactory,
    private final val isHtmlArgumentFactory: IsHtmlArgumentFactory
): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "fetchUserProfile"
    override fun create(input: String): Command {
        val actorArgument = actorArgumentFactory.create(input, true)
        val serverArgument = serverArgumentFactory.create(input).withDefault(
            ServerArgument(
                Server.Bancho
            )
        )
        val gameModeArgument = gameModeArgumentFactory.create(input)
        val isHtmlArgument = isHtmlArgumentFactory.create(input)

        return FetchUserProfileCommand(
            actorArgument = actorArgument,
            serverArgument = serverArgument,
            gameModeArgument = gameModeArgument,
            isHtmlArgument = isHtmlArgument
        )

    }
}