package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.api.osu.bancho.dto.OsuUser
import osu.salat23.circler.api.osu.exceptions.OsuUserNotFoundException
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.service.ChatService
import osu.salat23.circler.service.OsuService

@Component
class FetchUserInfoHandler(val osuService: OsuService, val chatService: ChatService) : ChainHandler() {
    // todo refactor these classes as the extension of GeneralChainHandler with getting identifier inside parent and provided services as is
    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        val identifier = getIdentifier(command, userContext, chatService, client)

        val osuApi = osuService.getOsuApiByServer(command.server)
        val user: OsuUser
        try {
            user = osuApi.user(identifier)
            client.send(
                ClientEntity.Builder()
                    .chatId(userContext.chatId)
                    .userId(userContext.userId)
                    .text(ResponseTemplates.osuUserTemplate(user, command))
                    .build()
            )
        } catch (exception: OsuUserNotFoundException) {
            client.send(
                ClientEntity.Builder()
                    .chatId(userContext.chatId)
                    .userId(userContext.userId)
                    .text(ResponseTemplates.osuUserNotFound(identifier))
                    .build()
            )
        }
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command.action == Command.Action.USER_GENERAL
    }
}