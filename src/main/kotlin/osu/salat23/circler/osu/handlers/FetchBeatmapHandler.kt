package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.service.OsuService

@Component
class FetchBeatmapHandler(val osuService: OsuService) : ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val osuApi = osuService.getOsuApiByServer(command.server)
        val beatmap = osuApi.beatmap(command.options.beatmapId)

        client.send(
            ClientMessage(
                chatId = userContext.chatId,
                userId = userContext.userId,
                text = ResponseTemplates.beatmapInfo(beatmap)
            )
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command.action == Command.Action.BEATMAP_LOOKUP
    }
}