package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.ScoreType
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientImage
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.impl.FetchUserRecentScoresCommand
import osu.salat23.circler.bot.response.templates.OldResponseTemplates
import osu.salat23.circler.service.OsuService
import osu.salat23.circler.service.PlayerIdentifierService
import java.net.URL

@Component
class FetchUserRecentScoresHandler(
    val osuService: OsuService,
    val playerIdentifierService: PlayerIdentifierService
) : ChainHandler() {

    override fun handleUpdate(command: Any, client: Client, clientBotContext: ClientBotContext) {
        val command = command as FetchUserRecentScoresCommand

        val identifier =
            playerIdentifierService.getIdentifier(command.actor, command.server, clientBotContext, client)
        val osuApi = osuService.getOsuApiByServer(command.server)
        val user = osuApi.user(identifier = identifier, gameMode = command.gameMode)
        val scores =
            osuApi.userScores(
                identifier = identifier,
                gameMode = command.gameMode,
                type = ScoreType.Recent,
                pageSize = command.pageSize,
                pageNumber = command.pageNumber,
                showFailed = true // todo option here
            )
        val text = if (scores.isEmpty()) "No recent scores found" else OldResponseTemplates.osuUserRecentScores(
            user,
            scores
        )
        if (command.pageSize == 1L && scores.isNotEmpty()) {
            val imageUrl = scores[0].beatmap.beatmapSet?.coverUrl ?: ""
            client.send(
                ClientImage(
                    chatId = clientBotContext.chatId,
                    userId = clientBotContext.userId,
                    text = text,
                    image = URL(imageUrl).openStream()
                )
            )
            return
        }
        client.send(
            ClientMessage(
                chatId = clientBotContext.chatId,
                userId = clientBotContext.userId,
                text = text
            )
        )
    }

    override fun canHandle(command: Any, clientBotContext: ClientBotContext): Boolean {
        return command is FetchUserRecentScoresCommand
    }
}