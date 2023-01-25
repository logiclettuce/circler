package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.factories.ServerArgumentFactory
import osu.salat23.circler.bot.command.commands.ChatLeaderboardCommand
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.exceptions.CommandIsNotDefinedException
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.configuration.domain.Command as ConfigCommand

@Component
class ChatLeaderboardCommandFactory(
    private val commandConfiguration: CommandConfiguration,
    private val serverArgumentFactory: ServerArgumentFactory
): CommandFactory() {

    private final val configuration: ConfigCommand
    companion object {
        private const val COMMAND_KEY = "chatLeaderboard"
    }
    init {
        if (!commandConfiguration.commands.containsKey(COMMAND_KEY))
            throw CommandIsNotDefinedException(COMMAND_KEY)
        configuration = commandConfiguration.commands[COMMAND_KEY]!!
    }
    override fun create(input: String): Command {
        var input = input

        // remove the action part
        for (actionIdentifier in configuration.identifiers) {
            if (input.startsWith("$actionIdentifier ")) {
                input  = input.substring(actionIdentifier.length+1)
                break
            }
        }


        val serverArgument = serverArgumentFactory.create(input)

        return ChatLeaderboardCommand(
            serverArgument = serverArgument
        )
    }

    override fun canCreate(input: String): Boolean {
        return CommandFactoryHelpers.canCreateBasic(input, COMMAND_KEY, commandConfiguration)
    }
}