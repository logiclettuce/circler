package osu.salat23.circler.osu.handlers

import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.osu.exceptions.UserNotDefinedException

abstract class ChainHandler {

    protected abstract fun handleUpdate(command: Any, client: Client, clientBotContext: ClientBotContext)
    protected abstract fun canHandle(command: Any, clientBotContext: ClientBotContext): Boolean
    fun checkAndHandle(command: Any, client: Client, clientBotContext: ClientBotContext) {
        val handleCheck = canHandle(command, clientBotContext)
        if (!handleCheck) return
        try {
            handleUpdate(command, client, clientBotContext)
        } catch (_: UserNotDefinedException) {
        }
    }
}