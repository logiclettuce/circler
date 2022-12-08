package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.osu.api.domain.models.OsuScore
import osu.salat23.circler.service.OsuService

@Component
class FetchUserRecentScoresHandler(val osuService: OsuService) : ChainHandler() {

    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        var identifier = command.options.actor
        if (identifier.isEmpty()) identifier = "salat23"

        val osuApi = osuService.getOsuApiByServer(command.server)
        val user = osuApi.user(identifier)
        val scores = osuApi.userScores(identifier, OsuScore.Type.Recent, command.options.pageSize, command.options.pageNumber)
        val text = if (scores.isEmpty()) "No recent scores found" else ResponseTemplates.osuUserRecentScores(user, command, scores)
        client.send(
            ClientEntity.Builder()
                .chatId(userContext.chatId)
                .userId(userContext.userId)
                .text(text)
                .build())
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command.action == Command.Action.USER_RECENT_SCORES
    }
}