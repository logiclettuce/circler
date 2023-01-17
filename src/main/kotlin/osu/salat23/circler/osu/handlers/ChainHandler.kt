package osu.salat23.circler.osu.handlers

import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.osu.exceptions.UserNotDefinedException
import osu.salat23.circler.service.ChatService
import osu.salat23.circler.service.UserServerIdentifierService

abstract class ChainHandler {

    protected abstract fun handleUpdate(command: Command, client: Client, userContext: UserContext)
    protected abstract fun canHandle(command: Command, userContext: UserContext): Boolean
    fun checkAndHandle(command: Command, client: Client, userContext: UserContext) {
        val handleCheck = canHandle(command, userContext)
        if (!handleCheck) return
        try {
            handleUpdate(command, client, userContext)
        } catch (_: UserNotDefinedException) {
        }
    }

    companion object {
        fun getIdentifier(
            command: Command,
            userContext: UserContext,
            userServerIdentifierService: UserServerIdentifierService,
            client: Client
        ): String {
            var identifier = command.options.actor
            if (identifier.isEmpty()) {
                val persistedIdentifier =
                    userServerIdentifierService
                        .getUserServerIdentifier(
                            userContext.userId,
                            userContext.chatId,
                            command.server,
                            userContext.clientType
                        )

                if (persistedIdentifier.isEmpty) {
                    client.send(
                        ClientMessage(
                            chatId = userContext.chatId,
                            userId = userContext.userId,
                            text = ResponseTemplates.osuUserNotDefined()
                        )
                    )
                    throw UserNotDefinedException()
                }
                identifier = persistedIdentifier.get()
            }
            return identifier
        }
    }
}