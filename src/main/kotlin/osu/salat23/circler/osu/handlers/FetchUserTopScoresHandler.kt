package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.ScoreType
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.impl.FetchUserTopScoresCommand
import osu.salat23.circler.bot.response.templates.OldResponseTemplates
import osu.salat23.circler.bot.response.templates.TemplateFactory
import osu.salat23.circler.service.OsuService
import osu.salat23.circler.service.PlayerIdentifierService

@Component
class FetchUserTopScoresHandler(
    val osuService: OsuService,
    val playerIdentifierService: PlayerIdentifierService,
    // todo make creatable commands
    val templateFactory: TemplateFactory
) : ChainHandler() {

    override fun handleUpdate(command: Any, client: Client, clientBotContext: ClientBotContext) {
        val command = command as FetchUserTopScoresCommand
        val identifier = playerIdentifierService.getIdentifier(command.actor, command.server, clientBotContext, client)

        val osuApi = osuService.getOsuApiByServer(command.server)
        val user = osuApi.user(identifier = identifier, gameMode = command.gameMode)
        val scores =
            osuApi.userScores(
                identifier = identifier,
                gameMode = command.gameMode,
                type = ScoreType.Best,
                pageSize = command.pageSize,
                pageNumber = command.pageNumber,
                showFailed = true // todo make option for this
            )

        val text = OldResponseTemplates.osuUserTopScores(user, command, scores)
//        val profileCommandCall = fetchUserProfileCommandFactory.produceCall(
//            actor = user.username,
//            server = server,
//            mode = gameMode,
//            isHtml = false // <============= change this
//        )
        client.send(
            ClientMessage(
                chatId = clientBotContext.chatId,
                userId = clientBotContext.userId,
                text = text,
               // furtherActions = listOf(FurtherAction(profileCommandCall, "Profile"))
            )
        )
    }

    override fun canHandle(command: Any, clientBotContext: ClientBotContext): Boolean {
        return command is FetchUserTopScoresCommand
    }
}