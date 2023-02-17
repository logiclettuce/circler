package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchBeatmapScoreCommand
import osu.salat23.circler.bot.response.browser.BrowserClient
import osu.salat23.circler.bot.response.context.SpecificContext
import osu.salat23.circler.bot.response.templates.TemplateFormat
import osu.salat23.circler.bot.response.templates.TemplateType
import osu.salat23.circler.service.ChatService
import osu.salat23.circler.service.OsuService
import osu.salat23.circler.service.PlayerIdentifierService

@Component
class FetchBeatmapScoreHandler(
    val osuService: OsuService,
    val chatService: ChatService,
    val browserClient: BrowserClient,
    val playerIdentifierService: PlayerIdentifierService
) : ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, clientBotContext: ClientBotContext) {
        val command = command as FetchBeatmapScoreCommand

        if (!command.beatmapIdArgument.isPresent()) {
            client.send(
                ClientMessage(
                    userId = clientBotContext.userId,
                    chatId = clientBotContext.chatId,
                    text = "No beatmapId provided!"
                )
            )
        }

        val server = command.beatmapIdArgument.getArgument().server
        val beatmapId = command.beatmapIdArgument.getArgument().id
        val gameMode = command.gameModeArgument.getArgument().mode
        val mods = command.modsArgument.getArgument().mods

        val chat = chatService.getOrCreateChat(clientBotContext.chatId, clientBotContext.clientType)
        val identifier = playerIdentifierService.getIdentifier(command.actorArgument, command.serverArgument, clientBotContext, client)

        val osuApi = osuService.getOsuApiByServer(server)
        val user = osuApi.user(identifier = identifier, gameMode = gameMode)
        val scores = osuApi.userBeatmapScores(
            identifier = identifier,
            gameMode = gameMode,
            beatmapId = beatmapId,
            requiredMods = mods
        )

        val context = SpecificContext.userScoresJson(user, server, scores)
        val textTemplate = chatService.getChatTemplateAndApplyContext(
            chat = chat,
            type = TemplateType.Scores,
            format = TemplateFormat.Text,
            context
        )

        client.send(
            ClientMessage(
                chatId = clientBotContext.chatId,
                userId = clientBotContext.userId,
                text = browserClient.process(textTemplate.value),
            )
        )
    }

    override fun canHandle(command: Command, clientBotContext: ClientBotContext): Boolean {
        return command is FetchBeatmapScoreCommand
    }
}