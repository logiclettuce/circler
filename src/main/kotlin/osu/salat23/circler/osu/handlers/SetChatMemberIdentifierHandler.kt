package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.impl.SetChatMemberIdentifierCommand
import osu.salat23.circler.service.OsuService
import osu.salat23.circler.service.UserServerIdentifierService

@Component
class SetChatMemberIdentifierHandler(val osuService: OsuService, val userServerIdentifierService: UserServerIdentifierService) : ChainHandler() {

    override fun handleUpdate(command: Any, client: Client, clientBotContext: ClientBotContext) {
        val command = command as SetChatMemberIdentifierCommand

        val osuApi = osuService.getOsuApiByServer(command.server)
        val exists = osuApi.playerExists(identifier = command.actor, gameMode = command.gameMode)
        if (exists) {
            userServerIdentifierService.setUserServerIdentifier(
                identifier = command.actor,
                userClientId = clientBotContext.userId,
                chatClientId = clientBotContext.chatId,
                clientType = clientBotContext.clientType,
                server = command.server
            )
            client.send(
                ClientMessage(
                    chatId = clientBotContext.chatId,
                    userId = clientBotContext.userId,
                    text = "Nickname has been set $command.actor"
                )
            )
            return
        }
        client.send(
            ClientMessage(
                chatId = clientBotContext.chatId,
                userId = clientBotContext.userId,
                text = "User does not exist $command.actor"
            )
        )
    }

    override fun canHandle(command: Any, clientBotContext: ClientBotContext): Boolean {
        return command is SetChatMemberIdentifierCommand
    }
}