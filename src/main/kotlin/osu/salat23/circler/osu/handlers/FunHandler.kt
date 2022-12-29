package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.commands.Command

@Component
class FunHandler : ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        client.send(
            ClientEntity.Builder()
                .chatId(userContext.chatId)
                .userId(userContext.userId)
                .text("Hello from fun handler!")
                .build()
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return command.action == Command.Action.FUN
    }
}