package osu.salat23.circler.osu.handlers

import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.osu.exceptions.UserNotDefinedException

abstract class ChainHandler {

    protected abstract fun handleUpdate(command: Command, client: Client, clientBotContext: ClientBotContext)
    protected abstract fun canHandle(command: Command, clientBotContext: ClientBotContext): Boolean
    fun checkAndHandle(command: Command, client: Client, clientBotContext: ClientBotContext) {
        val handleCheck = canHandle(command, clientBotContext)
        if (!handleCheck) return
        try {
            handleUpdate(command, client, clientBotContext)
        } catch (_: UserNotDefinedException) {
        }
    }
}