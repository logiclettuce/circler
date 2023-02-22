package osu.salat23.circler.service

import org.springframework.stereotype.Service
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.response.templates.OldResponseTemplates
import osu.salat23.circler.osu.exceptions.NoServerProvidedException
import osu.salat23.circler.osu.exceptions.UserNotDefinedException

@Service
class PlayerIdentifierService(
    val userServerIdentifierService: UserServerIdentifierService
) {

    fun getIdentifier(
        actor: String,
        server: Server,
        clientBotContext: ClientBotContext,
        client: Client
    ): String {
        var identifier = ""
        if (actor.isNotEmpty()) {
            identifier = actor
        }
        if (identifier.isEmpty()) {
            val persistedIdentifier =
                userServerIdentifierService
                    .getUserServerIdentifier(
                        clientBotContext.userId,
                        clientBotContext.chatId,
                        server,
                        clientBotContext.clientType
                    )

            if (persistedIdentifier.isEmpty) {
                client.send(
                    ClientMessage(
                        chatId = clientBotContext.chatId,
                        userId = clientBotContext.userId,
                        text = OldResponseTemplates.osuUserNotDefined()
                    )
                )
                throw UserNotDefinedException()
            }
            identifier = persistedIdentifier.get()
        }
        return identifier
    }

}