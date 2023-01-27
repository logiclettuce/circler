package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
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
    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {

    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command is FetchBeatmapScoreCommand
    }
}