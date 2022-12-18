package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.service.ChatService
import osu.salat23.circler.service.OsuService

@Component
class SetChatMemberIdentifier(val osuService: OsuService, val chatService: ChatService) : ChainHandler() {

    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        chatService.se
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command.action == Command.Action.SET_USER_SERVER_IDENTIFIER
    }
}