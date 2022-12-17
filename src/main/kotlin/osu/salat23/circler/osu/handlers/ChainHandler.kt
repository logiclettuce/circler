package osu.salat23.circler.osu.handlers

import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.osu.api.exceptions.UserNotDefinedException
import osu.salat23.circler.service.ChatService

abstract class ChainHandler {

    protected abstract fun handleUpdate(command: Command, client: Client, userContext: UserContext)
    protected abstract fun canHandle(command: Command, userContext: UserContext): Boolean
    fun checkAndHandle(command: Command, client: Client, userContext: UserContext) {
        val handleCheck = canHandle(command, userContext)
        if (!handleCheck) return
        handleUpdate(command, client, userContext)
    }

    companion object {
        fun getIdentifier(
            command: Command,
            userContext: UserContext,
            chatService: ChatService,
            client: Client
        ): String {
            var identifier = command.options.actor
            if (identifier.isEmpty()) {
                val persistedIdentifier =
                    chatService.getChatMemberServerPlayerIdentifier(
                        userContext.userId,
                        userContext.userId,
                        command.server,
                        userContext.chatType
                    )
                if (persistedIdentifier == null) {
                    client.send(
                        ClientEntity.Builder()
                            .chatId(userContext.chatId)
                            .userId(userContext.userId)
                            .text(ResponseTemplates.osuUserNotDefined())
                            .build()
                    )
                    throw UserNotDefinedException()
                }
                identifier = persistedIdentifier
            }
            return identifier
        }
    }
}