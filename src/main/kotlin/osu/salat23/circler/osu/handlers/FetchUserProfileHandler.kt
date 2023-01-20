package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.api.osu.exceptions.OsuUserNotFoundException
import osu.salat23.circler.bot.client.ClientImage
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.bot.commands.FetchUserProfileCommand
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.osu.domain.User
import osu.salat23.circler.renderer.BrowserClient
import osu.salat23.circler.renderer.ProfileRenderTemplate
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
    val userServerIdentifierService: UserServerIdentifierService) : ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val command = command as FetchUserProfileCommand
        val identifier = playerIdentifierService.getIdentifier(command.actorArgument, command.serverArgument, userContext, client)

        val osuApi = osuService.getOsuApiByServer(command.)
        val user: User
        try {
            user = osuApi.user(identifier)
            val clientEntity: ClientEntity = if (command.options.pictureMode) {
                val chat = chatService.getOrCreateChat(userContext.chatId, userContext.clientType)

                ClientImage(
                    chatId = userContext.chatId,
                    userId = userContext.userId,
                    image = browserClient.render(ProfileRenderTemplate(user, command, chat.userProfileTemplate))
                )
            } else {
                ClientMessage(
                    chatId = userContext.chatId,
                    userId = userContext.userId,
                    text = ResponseTemplates.osuUserTemplate(user, command)
                )
            }
            client.send(
                clientEntity
            )
        } catch (exception: OsuUserNotFoundException) {
            client.send(
                ClientMessage(
                    chatId = userContext.chatId,
                    userId = userContext.userId,
                    text = ResponseTemplates.osuUserNotFound(identifier)
                )
            )
        }
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command is FetchUserProfileCommand
    }

}