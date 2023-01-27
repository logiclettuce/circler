package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.GameModeArgument
import osu.salat23.circler.bot.command.arguments.factories.GameModeArgumentFactory
import osu.salat23.circler.bot.command.arguments.factories.ServerArgumentFactory
import osu.salat23.circler.bot.command.commands.ChatLeaderboardCommand
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.osu.domain.Mode

@Component
class ChatLeaderboardCommandFactory(
    commandConfiguration: CommandConfiguration,
    private val serverArgumentFactory: ServerArgumentFactory,
    private val gameModeArgumentFactory: GameModeArgumentFactory
): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "chatLeaderboard"
    override fun create(input: String): Command {
        val serverArgument = serverArgumentFactory.create(input, true)
        val gameModeArgument = gameModeArgumentFactory.create(input).withDefault(GameModeArgument(Mode.Default))

        return ChatLeaderboardCommand(
            serverArgument = serverArgument,
            gameModeArgument = gameModeArgument
        )
    }
}