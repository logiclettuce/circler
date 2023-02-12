package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchBeatmapScoreCommand
import osu.salat23.circler.service.OsuService
import osu.salat23.circler.service.PlayerIdentifierService

@Component
class FetchBeatmapScoreHandler(
    val osuService: OsuService,
    val playerIdentifierService: PlayerIdentifierService
): ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, clientBotContext: ClientBotContext) {
        val command = command as FetchBeatmapScoreCommand



        val server = command.serverArgument.getArgument()
        val beatmapId = command.beatmapIdArgument.getArgument()
    }

    override fun canHandle(command: Command, clientBotContext: ClientBotContext): Boolean {
        return command is FetchBeatmapScoreCommand
    }
}