package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.exceptions.OsuUserNotFoundException
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.client.ClientImage
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchUserProfileCommand
import osu.salat23.circler.bot.response.browser.BrowserClient
import osu.salat23.circler.bot.response.context.AdditionalContext
import osu.salat23.circler.bot.response.context.UserContext
import osu.salat23.circler.bot.response.templates.OldResponseTemplates
import osu.salat23.circler.bot.response.templates.ResponseTemplates
import osu.salat23.circler.bot.response.templates.Template
import osu.salat23.circler.bot.response.templates.TemplateFactory
import osu.salat23.circler.osu.domain.User
import osu.salat23.circler.service.ChatService
import osu.salat23.circler.service.OsuService
import osu.salat23.circler.service.PlayerIdentifierService
import osu.salat23.circler.service.UserServerIdentifierService

@Component
class FetchUserProfileHandler(
    val browserClient: BrowserClient,
    val osuService: OsuService,
    val chatService: ChatService,
    val playerIdentifierService: PlayerIdentifierService,
    val templateFactory: TemplateFactory,
    val userServerIdentifierService: UserServerIdentifierService) : ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, clientBotContext: ClientBotContext) {
        val command = command as FetchUserProfileCommand

        val server = command.serverArgument.getArgument().value
        val gameMode = command.gameModeArgument.getArgument().mode
        val isHtml = command.isHtmlArgument.getArgument().value

        // todo uhh maybe move out the logic from service to handler?
        val identifier = playerIdentifierService.getIdentifier(command.actorArgument, command.serverArgument, clientBotContext, client)

        val osuApi = osuService.getOsuApiByServer(server)
        val user: User
        try {
            user = osuApi.user(identifier = identifier, gameMode = gameMode)
            val chat = chatService.getOrCreateChat(clientBotContext.chatId, clientBotContext.clientType)
            val customTemplate = Template(chat.textProfileTemplate, chat.htmlProfileTemplate)
            val context = arrayOf(UserContext(user), AdditionalContext(server))
            val template = templateFactory.applyTemplateContext(
                templateType = ResponseTemplates.Profile,
                customTemplate = customTemplate,
                context = context
            )

            val clientEntity: ClientEntity = if (isHtml) {
                ClientImage(
                    chatId = clientBotContext.chatId,
                    userId = clientBotContext.userId,
                    image = browserClient.render(template.html)
                )
            } else {
                ClientMessage(
                    chatId = clientBotContext.chatId,
                    userId = clientBotContext.userId,
                    text = browserClient.process(template.text)
                )
            }
            client.send(
                clientEntity
            )
        } catch (exception: OsuUserNotFoundException) {
            client.send(
                ClientMessage(
                    chatId = clientBotContext.chatId,
                    userId = clientBotContext.userId,
                    text = OldResponseTemplates.osuUserNotFound(identifier)
                )
            )
        }
    }

    override fun canHandle(command: Command, clientBotContext: ClientBotContext): Boolean {
        return command is FetchUserProfileCommand
    }

}