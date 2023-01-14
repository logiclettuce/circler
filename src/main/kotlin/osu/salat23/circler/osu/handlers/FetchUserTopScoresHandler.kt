package osu.salat23.circler.osu.handlers

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.api.osu.OsuApi
import osu.salat23.circler.api.osu.OsuGameMode
import osu.salat23.circler.api.osu.bancho.dto.BanchoBeatmapAttributes
import osu.salat23.circler.api.osu.bancho.dto.OsuScore
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.service.ChatService
import osu.salat23.circler.service.OsuService
import java.util.Collections


@Component
class FetchUserTopScoresHandler(val osuService: OsuService, val chatService: ChatService) : ChainHandler() {

    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val identifier = getIdentifier(command, userContext, chatService, client)

        val osuApi = osuService.getOsuApiByServer(command.server)
        val user = osuApi.user(identifier)
        val scores =
            osuApi.userScores(identifier, OsuScore.Type.Best, command.options.pageSize, command.options.pageNumber)
        //val scoresWithBeatmapAttributes =  getScoresWithBeatmapAttributes(scores, osuApi)

        val text = ResponseTemplates.osuUserTopScores(user, command, scores)
        client.send(
            ClientMessage(
                chatId = userContext.chatId,
                userId = userContext.userId,
                text = text
            )
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        val canHandle = when (command.action) {
            Command.Action.USER_TOP_SCORES -> true
            else -> false
        }
        return canHandle
    }
}