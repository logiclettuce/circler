package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage

@Component
class FunHandler : ChainHandler() {
    override fun handleUpdate(command: Any, client: Client, clientBotContext: ClientBotContext) {
        client.send(
            ClientMessage(
                chatId = clientBotContext.chatId,
                userId = clientBotContext.userId,
                text = "Hello from fun handler!"
            )
        )
    }

    override fun canHandle(command: Any, clientBotContext: ClientBotContext): Boolean {
        return false
    }
}