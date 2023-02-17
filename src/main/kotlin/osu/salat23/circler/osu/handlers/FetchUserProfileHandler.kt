package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.exceptions.OsuUserNotFoundException
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.*
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.UserProfileCommand
import osu.salat23.circler.bot.command.commands.factories.FetchUserProfileCommandFactory
import osu.salat23.circler.bot.response.browser.BrowserClient
import osu.salat23.circler.bot.response.context.SpecificContext
import osu.salat23.circler.bot.response.templates.*
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
    val fetchUserProfileCommandFactory: FetchUserProfileCommandFactory,
    val templateFactory: TemplateFactory,
    val userServerIdentifierService: UserServerIdentifierService) : ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, clientBotContext: ClientBotContext) {
        val command = command as UserProfileCommand

        val server = command.serverArgument.getArgument().value
        val gameMode = command.gameModeArgument.getArgument().mode
        val doRender = command.isHtmlArgument.getArgument().value

        // todo uhh maybe move out the logic from service to handler?
        val identifier = playerIdentifierService.getIdentifier(command.actorArgument, command.serverArgument, clientBotContext, client)

        val osuApi = osuService.getOsuApiByServer(server)
        val user: User
        try {
            user = osuApi.user(identifier = identifier, gameMode = gameMode)
            val chat = chatService.getOrCreateChat(clientBotContext.chatId, clientBotContext.clientType)
            val context = SpecificContext.userProfileJson(user, server)

            val clientEntity: ClientEntity = if (doRender) {
                val htmlTemplate = chatService.getChatTemplateAndApplyContext(
                    chat = chat,
                    type = TemplateType.Profile,
                    format = TemplateFormat.Html,
                    context
                )

                ClientImage(
                    chatId = clientBotContext.chatId,
                    userId = clientBotContext.userId,
                    image = browserClient.render(htmlTemplate.value),
                )
            } else {
                val textTemplate = chatService.getChatTemplateAndApplyContext(
                    chat = chat,
                    type = TemplateType.Profile,
                    format = TemplateFormat.Text,
                    context
                )

                ClientMessage(
                    chatId = clientBotContext.chatId,
                    userId = clientBotContext.userId,
                    text = browserClient.process(textTemplate.value),
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
        return command is UserProfileCommand
    }

}