package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.ScoreType
import osu.salat23.circler.api.osu.bancho.dto.BanchoScore
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchUserTopScoresCommand
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.service.OsuService
import osu.salat23.circler.service.PlayerIdentifierService
import osu.salat23.circler.service.UserServerIdentifierService


@Component
class FetchUserTopScoresHandler(
    val osuService: OsuService,
    val playerIdentifierService: PlayerIdentifierService) : ChainHandler() {

    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val command = command as FetchUserTopScoresCommand
        val identifier = playerIdentifierService.getIdentifier(command.actorArgument, command.serverArgument, userContext, client)
        val server = command.serverArgument.getArgument().value
        val gameMode = command.gameModeArgument.getArgument().mode
        val pageSize = command.pageSizeArgument.getArgument().value
        val pageNumber = command.pageArgument.getArgument().value

        val osuApi = osuService.getOsuApiByServer(server)
        val user = osuApi.user(identifier = identifier, gameMode = gameMode)
        val scores =
            osuApi.userScores(
                identifier = identifier,
                gameMode = gameMode,
                type = ScoreType.Best,
                pageSize = pageSize,
                pageNumber = pageNumber,
                showFailed = true // todo make option for this
            )

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
        return command is FetchUserTopScoresCommand
    }
}