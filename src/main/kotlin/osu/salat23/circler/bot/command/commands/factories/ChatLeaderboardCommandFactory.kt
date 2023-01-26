package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.factories.ServerArgumentFactory
import osu.salat23.circler.bot.command.commands.ChatLeaderboardCommand
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.configuration.domain.CommandConfiguration

@Component
class ChatLeaderboardCommandFactory(
    commandConfiguration: CommandConfiguration,
    private val serverArgumentFactory: ServerArgumentFactory
): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "chatLeaderboard"
    override fun create(input: String): Command {
        val serverArgument = serverArgumentFactory.create(input)

        return ChatLeaderboardCommand(
            serverArgument = serverArgument
        )
    }
}