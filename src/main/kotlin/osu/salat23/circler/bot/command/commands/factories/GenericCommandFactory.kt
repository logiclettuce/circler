package osu.salat23.circler.bot.command.commands.factories

import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.exceptions.CommandIsNotDefinedException
import osu.salat23.circler.configuration.domain.CommandConfiguration
import java.util.*
import osu.salat23.circler.configuration.domain.Command as ConfigCommand

abstract class GenericCommandFactory(
    val commandConfiguration: CommandConfiguration
): CommandFactory() {
    protected val configuration: ConfigCommand
    init {
        if (!commandConfiguration.commands.containsKey(getCommandKey()))
            throw CommandIsNotDefinedException(getCommandKey())
        configuration = commandConfiguration.commands[getCommandKey()]!!
    }
    abstract fun getCommandKey(): String
    override fun canCreate(input: String): Boolean {
        var canCreate = false
        val commandActionIdentifiers = commandConfiguration.commands[getCommandKey()]!!.identifiers

        commandActionIdentifiers.forEach { identifier ->
            if (input.lowercase().startsWith(identifier.lowercase()) && input.length == identifier.length)
                canCreate = true

            if (input.lowercase().startsWith(identifier.lowercase() + ' '))
                canCreate = true
        }

        return canCreate
    }

    override fun checkAndCreate(input: String): Optional<Command> {
        if (canCreate(input)) {
            var input = input

            // remove the action part
            for (actionIdentifier in configuration.identifiers) {
                if (input.length == actionIdentifier.length) {
                    if (input.lowercase().startsWith(actionIdentifier.lowercase())) {
                        input  = input.substring(actionIdentifier.length)
                        break
                    }
                } else {
                    if (input.lowercase().startsWith("${actionIdentifier.lowercase()} ")) {
                        input  = input.substring(actionIdentifier.length+1)
                        break
                    }
                }
            }
            // now input contains only argument part
            return Optional.of(create(input))
        }
        return Optional.empty()
    }
}