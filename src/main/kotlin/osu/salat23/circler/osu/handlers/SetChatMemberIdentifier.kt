package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.service.ChatService
import osu.salat23.circler.service.OsuService
import osu.salat23.circler.service.UserServerIdentifierService

@Component
class SetChatMemberIdentifier(val osuService: OsuService, val userServerIdentifierService: UserServerIdentifierService) : ChainHandler() {

    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val osuApi = osuService.getOsuApiByServer(command.server)
        val exists = osuApi.playerExists(command.options.actor)
        if (exists) {
            userServerIdentifierService.setUserServerIdentifier(
                identifier = command.options.actor,
                userClientId = userContext.userId,
                chatClientId = userContext.chatId,
                clientType = userContext.clientType,
                server = command.server
            )
            client.send(
                ClientMessage(
                    chatId = userContext.chatId,
                    userId = userContext.userId,
                    text = "Nickname has been set ${command.options.actor}"
                )
            )
            return
        }
        client.send(
            ClientMessage(
                chatId = userContext.chatId,
                userId = userContext.userId,
                text = "User does not exist ${command.options.actor}"
            )
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command.action == Command.Action.SET_USER_SERVER_IDENTIFIER
    }
}