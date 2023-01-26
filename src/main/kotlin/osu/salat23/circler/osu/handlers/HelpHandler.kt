package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.commands.Command

@Component
class HelpHandler : ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {

        client.send(
            ClientMessage(
                chatId = userContext.chatId,
                userId = userContext.userId,
                text = "Work in progress!"
            )
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return false
    }
}