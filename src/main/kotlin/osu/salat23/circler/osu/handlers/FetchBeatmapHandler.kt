package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchBeatmapCommand
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.service.OsuService

@Component
class FetchBeatmapHandler(val osuService: OsuService) : ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val command = command as FetchBeatmapCommand

        if (!command.beatmapIdArgument.isPresent()) {
            client.send(
                ClientMessage(
                    chatId = userContext.chatId,
                    userId = userContext.userId,
                    text = "No beatmap id provided"
                )
            )
        }

        val beatmapId = command.beatmapIdArgument.getArgument().id
        val gameMode = command.gameModeArgument.getArgument().mode
        val mods = command.modsArgument.getArgument().mods


        val osuApi = osuService.getOsuApiByServer(Server.Bancho) // todo maybe later add other servers for fetching??
        val beatmap = osuApi.beatmap(id = beatmapId, gameMode = gameMode, mods = mods)

        client.send(
            ClientMessage(
                chatId = userContext.chatId,
                userId = userContext.userId,
                text = ResponseTemplates.beatmapInfo(beatmap)
            )
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command is FetchBeatmapCommand
    }
}