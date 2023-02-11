package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.ScoreType
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.client.FurtherAction
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchUserTopScoresCommand
import osu.salat23.circler.bot.command.commands.factories.FetchUserProfileCommandFactory
import osu.salat23.circler.bot.response.context.SpecificContext
import osu.salat23.circler.bot.response.templates.OldResponseTemplates
import osu.salat23.circler.bot.response.templates.TemplateType
import osu.salat23.circler.bot.response.templates.Template
import osu.salat23.circler.bot.response.templates.TemplateFactory
import osu.salat23.circler.service.OsuService
import osu.salat23.circler.service.PlayerIdentifierService


@Component
class FetchUserTopScoresHandler(
    val osuService: OsuService,
    val playerIdentifierService: PlayerIdentifierService,
    val fetchUserProfileCommandFactory: FetchUserProfileCommandFactory,
    val templateFactory: TemplateFactory
) : ChainHandler() {

    override fun handleUpdate(command: Command, client: Client, clientBotContext: ClientBotContext) {
        val command = command as FetchUserTopScoresCommand
        val identifier = playerIdentifierService.getIdentifier(command.actorArgument, command.serverArgument, clientBotContext, client)
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

        val text = OldResponseTemplates.osuUserTopScores(user, command, scores)
        val profileCommandCall = fetchUserProfileCommandFactory.produceCall(
            actor = user.username,
            server = server,
            mode = gameMode,
            isHtml = false // <============= change this
        )
        client.send(
            ClientMessage(
                chatId = clientBotContext.chatId,
                userId = clientBotContext.userId,
                text = text,
                furtherActions = listOf(FurtherAction(profileCommandCall, "Profile"))
            )
        )
    }

    override fun canHandle(command: Command, clientBotContext: ClientBotContext): Boolean {
        return command is FetchUserTopScoresCommand
    }
}