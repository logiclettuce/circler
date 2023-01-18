package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.api.osu.bancho.dto.BanchoScore
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.service.OsuService
import osu.salat23.circler.service.UserServerIdentifierService


@Component
class FetchUserTopScoresHandler(val osuService: OsuService, val userServerIdentifierService: UserServerIdentifierService) : ChainHandler() {

    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val identifier = getIdentifier(command, userContext, userServerIdentifierService, client)

        val osuApi = osuService.getOsuApiByServer(command.server)
        val user = osuApi.user(identifier)
        val scores =
            osuApi.userScores(identifier, BanchoScore.Type.Best, command.options.pageSize, command.options.pageNumber)
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