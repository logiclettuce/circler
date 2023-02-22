package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.impl.FetchBeatmapScoresCommand
import osu.salat23.circler.bot.response.browser.BrowserClient
import osu.salat23.circler.bot.response.context.SpecificContext
import osu.salat23.circler.bot.response.templates.TemplateFormat
import osu.salat23.circler.bot.response.templates.TemplateType
import osu.salat23.circler.osu.domain.Mod
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
    override fun handleUpdate(command: Any, client: Client, clientBotContext: ClientBotContext) {
        val command = command as FetchBeatmapScoresCommand

        val mods = Mod.fromString(command.mods)

        val chat = chatService.getOrCreateChat(clientBotContext.chatId, clientBotContext.clientType)
        val identifier = playerIdentifierService.getIdentifier(command.actor, command.server, clientBotContext, client)

        val osuApi = osuService.getOsuApiByServer(command.server)
        val user = osuApi.user(identifier = identifier, gameMode = command.gameMode)
        val scores = osuApi.userBeatmapScores(
            identifier = identifier,
            gameMode = command.gameMode,
            beatmapId = command.beatmapId,
            requiredMods = mods
        )

        val context = SpecificContext.userScoresJson(user, command.server, scores)
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

    override fun canHandle(command: Any, clientBotContext: ClientBotContext): Boolean {
        return command is FetchBeatmapScoresCommand
    }
}