package osu.salat23.circler.osu.handlers

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.impl.MapChatLeaderboardCommand
import osu.salat23.circler.bot.response.templates.OldResponseTemplates
import osu.salat23.circler.osu.domain.Mod
import osu.salat23.circler.osu.domain.Score
import osu.salat23.circler.osu.domain.User
import osu.salat23.circler.service.ChatService
import osu.salat23.circler.service.OsuService

@Component
class MapChatLeaderboardHandler(
    val osuService: OsuService,
    val chatService: ChatService
) : ChainHandler() {
    override fun handleUpdate(command: Any, client: Client, clientBotContext: ClientBotContext) {
        val command = command as MapChatLeaderboardCommand

        val mods = Mod.fromString(command.mods)

        val osuApi = osuService.getOsuApiByServer(command.server)

        val map = osuApi.beatmap(id = command.beatmapId, gameMode = command.gameMode, mods = mods)

        val identifiers = chatService.getChatMemberIdentifiers(
            clientId = clientBotContext.chatId,
            clientType = clientBotContext.clientType,
            server = command.server
        )
        // todo IMPORTANT! fix values presentation (probably convert problem)
        val userScorePairs: MutableList<Pair<User, Score>> = mutableListOf()
        runBlocking {
            // todo implement filtering by mods logic
            identifiers.parallelStream().forEach { identifier ->
                val user = osuApi.user(identifier = identifier, gameMode = command.gameMode)
                val userBeatmapScores = osuApi.userBeatmapScores(
                    identifier = user.username,
                    gameMode = command.gameMode,
                    beatmapId = map.id,
                    requiredMods = mods // todo mods filtering logic
                )
                if (userBeatmapScores.isNotEmpty()) {
                    val bestScore: Score = userBeatmapScores.reduce { acc, score ->
                        if (score.score > acc.score) return@reduce score
                        return@reduce acc
                    }
                    synchronized(userScorePairs) {
                        userBeatmapScores.forEach { score ->
                            userScorePairs.add(Pair(user, score))
                        }
                       // userScorePairs.add(Pair(user, bestScore))
                    }
                }
            }
        }
        val finalUserScorePairs = userScorePairs.sortedBy { it.second.score }.reversed()

        client.send(
            ClientMessage(
                userId = clientBotContext.userId,
                chatId = clientBotContext.chatId,
                text = OldResponseTemplates.chatBeatmapLeaderboard(map, finalUserScorePairs)
            )
        )

    }

    override fun canHandle(command: Any, clientBotContext: ClientBotContext): Boolean {
        return command is MapChatLeaderboardCommand
    }
}