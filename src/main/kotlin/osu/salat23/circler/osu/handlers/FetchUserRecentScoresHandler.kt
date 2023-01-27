package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.ScoreType
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.api.osu.bancho.dto.BanchoScore
import osu.salat23.circler.bot.client.ClientImage
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.commands.FetchUserRecentScoresCommand
import osu.salat23.circler.service.OsuService
import osu.salat23.circler.service.PlayerIdentifierService
import osu.salat23.circler.service.UserServerIdentifierService
import java.net.URL

@Component
class FetchUserRecentScoresHandler(
    val osuService: OsuService,
    val playerIdentifierService: PlayerIdentifierService
) : ChainHandler() {

    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val command = command as FetchUserRecentScoresCommand

        if (!command.serverArgument.isPresent()) {
            client.send(
                ClientMessage(
                    chatId = userContext.chatId,
                    userId = userContext.userId,
                    text = "No server provided!"
                )
            )
            return
        }
        val server = command.serverArgument.getArgument().value
        val gameMode = command.gameModeArgument.getArgument().mode
        val pageSize = command.pageSizeArgument.getArgument().value
        val pageNumber = command.pageArgument.getArgument().value

        val identifier =
            playerIdentifierService.getIdentifier(command.actorArgument, command.serverArgument, userContext, client)
        val osuApi = osuService.getOsuApiByServer(server)
        val user = osuApi.user(identifier = identifier, gameMode = gameMode)
        val scores =
            osuApi.userScores(
                identifier = identifier,
                gameMode = gameMode,
                type = ScoreType.Recent,
                pageSize = pageSize,
                pageNumber = pageNumber,
                showFailed = true // todo option here
            )
        val text = if (scores.isEmpty()) "No recent scores found" else ResponseTemplates.osuUserRecentScores(
            user,
            command,
            scores
        )
        if (pageSize == 1L && scores.isNotEmpty()) {
            val imageUrl = scores[0].beatmap.beatmapSet?.coverUrl ?: ""
            client.send(
                ClientImage(
                    chatId = userContext.chatId,
                    userId = userContext.userId,
                    text = text,
                    image = URL(imageUrl).openStream()
                )
            )
            return
        }
        client.send(
            ClientMessage(
                chatId = userContext.chatId,
                userId = userContext.userId,
                text = text
            )
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command is FetchUserRecentScoresCommand
    }
}