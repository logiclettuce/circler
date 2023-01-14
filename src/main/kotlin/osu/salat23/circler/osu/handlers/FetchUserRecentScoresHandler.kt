package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.api.osu.bancho.dto.OsuScore
import osu.salat23.circler.bot.client.ClientImage
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.service.ChatService
import osu.salat23.circler.service.OsuService
import java.io.InputStream
import java.net.URL

@Component
class FetchUserRecentScoresHandler(val osuService: OsuService, val chatService: ChatService) : ChainHandler() {

    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val identifier = getIdentifier(command, userContext, chatService, client)
        val osuApi = osuService.getOsuApiByServer(command.server)
        val user = osuApi.user(identifier)
        val scores =
            osuApi.userScores(identifier, OsuScore.Type.Recent, command.options.pageSize, command.options.pageNumber)
        val text = if (scores.isEmpty()) "No recent scores found" else ResponseTemplates.osuUserRecentScores(
            user,
            command,
            scores
        )
        if (command.options.pageSize == 1 && scores.isNotEmpty()) {
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
        return command.action == Command.Action.USER_RECENT_SCORES
    }
}