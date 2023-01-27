package osu.salat23.circler.osu.handlers

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.commands.ChatLeaderboardCommand
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.osu.domain.User
import osu.salat23.circler.pmap
import osu.salat23.circler.service.ChatService
import osu.salat23.circler.service.OsuService


@Component
class ChatLeaderboardHandler(val chatService: ChatService, val osuService: OsuService): ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val command = command as ChatLeaderboardCommand

        if (!command.serverArgument.isPresent()) {
            client.send(
                ClientMessage(
                    userId = userContext.userId,
                    chatId = userContext.chatId,
                    text = "No server provided!"
                )
            )
        }

        val server = command.serverArgument.getArgument().value
        val gameMode = command.gameModeArgument.getArgument().mode

        val osuApi = osuService.getOsuApiByServer(server)

        val chatMemberIdentifiers = chatService.getChatMemberIdentifiers(userContext.chatId, userContext.clientType, server)

        var users: List<User>
        runBlocking {
            users = chatMemberIdentifiers.pmap { osuApi.user(identifier = it, gameMode) }
        }

        users = users.sortedWith(Comparator { u1, u2 ->
            if (u1.performance == u2.performance) return@Comparator 0
            if (u1.performance > u2.performance ) return@Comparator 1
            return@Comparator -1
        }).reversed()

        client.send(
            ClientMessage(
                userId = userContext.userId,
                chatId = userContext.chatId,
                text = ResponseTemplates.chatLeaderboard(users)
            )
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command is ChatLeaderboardCommand
    }
}