package osu.salat23.circler.osu.handlers

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.osu.domain.Score
import osu.salat23.circler.osu.domain.User
import osu.salat23.circler.service.ChatService
import osu.salat23.circler.service.OsuService

@Component
class MapChatLeaderboardHandler(
    val osuService: OsuService,
    val chatService: ChatService
) : ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val osuApi = osuService.getOsuApiByServer(command.server)

        val map = osuApi.beatmap(command.options.beatmapId)

        val identifiers = chatService.getChatMemberIdentifiers(
            clientId = userContext.chatId,
            clientType = userContext.clientType,
            server = command.server
        )
        // todo IMPORTANT! fix values presentation (probably convert problem)
        var userScorePairs: MutableList<Pair<User, Score>> = mutableListOf()
        runBlocking {
            // todo implement filtering by mods logic
            identifiers.parallelStream().forEach { identifier ->
                val user = osuApi.user(identifier)
                var userBeatmapScores = osuApi.userBeatmapScores(user.username, map.id)
                if (userBeatmapScores.isNotEmpty()) {

                    val bestScore: Score = userBeatmapScores.reduce { acc, score ->
                        if (score.score > acc.score) return@reduce score
                        return@reduce acc
                    }
                    synchronized(userScorePairs) {
                        userScorePairs.add(Pair(user, bestScore))
                    }
                }
            }
        }
        val finalUserScorePairs = userScorePairs.sortedBy { it.second.score }.reversed()

        client.send(
            ClientMessage(
                userId = userContext.userId,
                chatId = userContext.chatId,
                text = ResponseTemplates.chatBeatmapLeaderboard(map, finalUserScorePairs)
            )
        )

    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command.action == Command.Action.MAP_CHAT_LEADERBOARD
    }
}