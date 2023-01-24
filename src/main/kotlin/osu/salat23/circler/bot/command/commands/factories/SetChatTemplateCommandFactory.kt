package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.NumberArgument
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.arguments.StringArgument
import osu.salat23.circler.bot.command.arguments.factories.TemplateArgumentFactory
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchUserTopScoresCommand
import osu.salat23.circler.bot.command.commands.SetChatTemplateCommand
import osu.salat23.circler.bot.command.exceptions.CommandIsNotDefinedException
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.osu.Server
import osu.salat23.circler.configuration.domain.Command as ConfigCommand

@Component
class SetChatTemplateCommandFactory(
    private val commandConfiguration: CommandConfiguration,
    private val templateArgumentFactory: TemplateArgumentFactory
    ): CommandFactory() {

    private final val configuration: ConfigCommand
    companion object {
        private const val COMMAND_KEY = "setChatTemplate"
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

        val templateArgument = templateArgumentFactory.create(input, true).withDefault(StringArgument(""))

        return SetChatTemplateCommand(
            templateArgument = templateArgument
        )
    }

    override fun canCreate(input: String): Boolean {
        var canCreate = false
        val commandActionIdentifiers = commandConfiguration.commands[COMMAND_KEY]!!.identifiers

        commandActionIdentifiers.forEach { identifier ->
            if (input.lowercase().startsWith(identifier.lowercase() + ' '))
                canCreate = true
        }

        return canCreate
    }
}