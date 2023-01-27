package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.SetChatMemberIdentifierCommand
import osu.salat23.circler.service.OsuService
import osu.salat23.circler.service.UserServerIdentifierService

@Component
class SetChatMemberIdentifierHandler(val osuService: OsuService, val userServerIdentifierService: UserServerIdentifierService) : ChainHandler() {

    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val command = command as SetChatMemberIdentifierCommand

        if (!command.actorArgument.isPresent()) {
            client.send(
                ClientMessage(
                    chatId = userContext.chatId,
                    userId = userContext.userId,
                    text = "No username provided!"
                )
            )
        }

        val gameMode = command.gameModeArgument.getArgument().mode

        val actor = command.actorArgument.getArgument().value
        val server = command.serverArgument.getArgument().value

        val osuApi = osuService.getOsuApiByServer(server)
        val exists = osuApi.playerExists(identifier = actor, gameMode = gameMode)
        if (exists) {
            userServerIdentifierService.setUserServerIdentifier(
                identifier = actor,
                userClientId = userContext.userId,
                chatClientId = userContext.chatId,
                clientType = userContext.clientType,
                server = server
            )
            client.send(
                ClientMessage(
                    chatId = userContext.chatId,
                    userId = userContext.userId,
                    text = "Nickname has been set $actor"
                )
            )
            return
        }
        client.send(
            ClientMessage(
                chatId = userContext.chatId,
                userId = userContext.userId,
                text = "User does not exist $actor"
            )
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command is SetChatMemberIdentifierCommand
    }
}