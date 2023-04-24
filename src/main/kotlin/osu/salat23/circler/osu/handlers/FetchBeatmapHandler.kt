package osu.salat23.circler.osu.handlers

import org.springframework.context.support.beans
import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.api.osu.exceptions.RequestFailedException
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.impl.FetchBeatmapCommand
import osu.salat23.circler.bot.response.templates.OldResponseTemplates
import osu.salat23.circler.osu.domain.Mod
import osu.salat23.circler.service.OsuService

@Component
class FetchBeatmapHandler(val osuService: OsuService) : ChainHandler() {
    override fun handleUpdate(command: Any, client: Client, clientBotContext: ClientBotContext) {
        val command = command as FetchBeatmapCommand

        val mods = Mod.fromString(command.mods)


        val osuApi = osuService.getOsuApiByServer(Server.Bancho) // todo maybe later add other servers for fetching??
        val beatmap = try { osuApi.beatmap(id = command.beatmapId, gameMode = command.gameMode, mods = mods) } catch (exception: RequestFailedException) { null }

        if (beatmap == null) {
            client.send(
                ClientMessage(
                    chatId = clientBotContext.chatId,
                    userId = clientBotContext.userId,
                    text = "Beatmap not found!"
                )
            )
            return
        }

        client.send(
            ClientMessage(
                chatId = clientBotContext.chatId,
                userId = clientBotContext.userId,
                text = OldResponseTemplates.beatmapInfo(beatmap)
            )
        )
    }

    override fun canHandle(command: Any, clientBotContext: ClientBotContext): Boolean {
        return command is FetchBeatmapCommand
    }
}