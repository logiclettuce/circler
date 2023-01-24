package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
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

        val osuApi = osuService.getOsuApiByServer(command.serverArgument.getArgument().value)
        val user = osuApi.user(identifier)
        val scores =
            osuApi.userScores(
                identifier,
                BanchoScore.Type.Best,
                command.pageSizeArgument.getArgument().value,
                command.pageArgument.getArgument().value)

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