package osu.salat23.circler.osu.handlers

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.MapChatLeaderboardCommand
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
        val command = command as MapChatLeaderboardCommand
        if (!command.beatmapIdArgument.isPresent()) {
            client.send(
                ClientMessage(
                    chatId = userContext.chatId,
                    userId = userContext.userId,
                    text = "No beatmap provided!"
                )
            )
            return
        }

        val server = command.serverArgument.getArgument().value
        val beatmapId = command.beatmapIdArgument.getArgument().id
        val gameMode = command.gameModeArgument.getArgument().mode

        val modsArgument = command.modsArgument.getArgument()

        val osuApi = osuService.getOsuApiByServer(server)

        val map = osuApi.beatmap(id = beatmapId, gameMode = gameMode, mods = modsArgument.mods)

        val identifiers = chatService.getChatMemberIdentifiers(
            clientId = userContext.chatId,
            clientType = userContext.clientType,
            server = server
        )
        // todo IMPORTANT! fix values presentation (probably convert problem)
        val userScorePairs: MutableList<Pair<User, Score>> = mutableListOf()
        runBlocking {
            // todo implement filtering by mods logic
            identifiers.parallelStream().forEach { identifier ->
                val user = osuApi.user(identifier = identifier, gameMode = gameMode)
                val userBeatmapScores = osuApi.userBeatmapScores(
                    identifier = user.username,
                    gameMode = gameMode,
                    beatmapId = map.id,
                    requiredMods = modsArgument.mods // todo mods filtering logic
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
                userId = userContext.userId,
                chatId = userContext.chatId,
                text = ResponseTemplates.chatBeatmapLeaderboard(map, finalUserScorePairs)
            )
        )

    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command is MapChatLeaderboardCommand
    }
}