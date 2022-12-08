package osu.salat23.circler.osu.handlers

import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.commands.Command

abstract class ChainHandler {
    protected abstract fun handleUpdate(command: Command, client: Client, userContext: UserContext)
    protected abstract fun canHandle(command: Command, userContext: UserContext): Boolean
    fun checkAndHandle(command: Command, client: Client, userContext: UserContext) {
        val handleCheck = canHandle(command, userContext)
        if (!handleCheck) return
        handleUpdate(command, client, userContext)
    }
}