package osu.salat23.circler.bot.command.commands.factories

import osu.salat23.circler.configuration.domain.CommandConfiguration

object CommandFactoryHelpers {

    /*
      todo probably should create interface and implement classes. would be much better imo.
            and also redo part with removing action part of the input in all of the factories.
     */
    fun canCreateBasic(input: String, commandKey: String, commandConfiguration: CommandConfiguration): Boolean {
        var canCreate = false
        val commandActionIdentifiers = commandConfiguration.commands[commandKey]!!.identifiers

        commandActionIdentifiers.forEach { identifier ->
            if (input.lowercase().startsWith(identifier.lowercase() + ' '))
                canCreate = true
        }

        return canCreate
    }

}