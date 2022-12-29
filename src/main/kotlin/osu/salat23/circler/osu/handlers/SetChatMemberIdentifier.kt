package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.service.ChatService
import osu.salat23.circler.service.OsuService

@Component
class SetChatMemberIdentifier(val osuService: OsuService, val chatService: ChatService) : ChainHandler() {

    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val osuApi = osuService.getOsuApiByServer(command.server)
        val exists = osuApi.playerExists(command.options.actor)
        if (exists) {
            chatService.setPlayerIdentifier(
                identifier = command.options.actor,
                userId = userContext.userId,
                chatId = userContext.chatId,
                clientType = userContext.clientType,
                server = command.server
            )
            client.send(
                ClientEntity.Builder()
                    .chatId(userContext.chatId)
                    .userId(userContext.userId)
                    .text("Nickname has been set ${command.options.actor}")
                    .build()
            )
            return
        }
        client.send(
            ClientEntity.Builder()
                .chatId(userContext.chatId)
                .userId(userContext.userId)
                .text("User does not exist ${command.options.actor}")
                .build()
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command.action == Command.Action.SET_USER_SERVER_IDENTIFIER
    }
}