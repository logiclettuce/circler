package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.api.osu.bancho.dto.OsuUser
import osu.salat23.circler.api.osu.exceptions.OsuUserNotFoundException
import osu.salat23.circler.bot.client.ClientImage
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.osu.domain.User
import osu.salat23.circler.renderer.BrowserClient
import osu.salat23.circler.renderer.ProfileRenderTemplate
import osu.salat23.circler.service.ChatService
import osu.salat23.circler.service.OsuService

@Component
class FetchUserInfoHandler(val browserClient: BrowserClient, val osuService: OsuService, val chatService: ChatService) : ChainHandler() {
    // todo refactor these classes as the extension of GeneralChainHandler with getting identifier inside parent and provided services as is
    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val identifier = getIdentifier(command, userContext, chatService, client)

        val osuApi = osuService.getOsuApiByServer(command.server)
        val user: User
        try {
            user = osuApi.user(identifier)
            val clientEntity: ClientEntity = if (command.options.pictureMode) {
                ClientImage(
                    chatId = userContext.chatId,
                    userId = userContext.userId,
                    image = browserClient.render(ProfileRenderTemplate(user, command))
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
        return command.action == Command.Action.USER_GENERAL
    }
}