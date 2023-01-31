package osu.salat23.circler.service

import org.springframework.stereotype.Service
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.arguments.ProvidedArgument
import osu.salat23.circler.bot.command.arguments.ServerArgument
import osu.salat23.circler.bot.command.arguments.StringArgument
import osu.salat23.circler.bot.response.templates.OldResponseTemplates
import osu.salat23.circler.osu.exceptions.NoServerProvidedException
import osu.salat23.circler.osu.exceptions.UserNotDefinedException

@Service
class PlayerIdentifierService(
    val userServerIdentifierService: UserServerIdentifierService
) {

    fun getIdentifier(
        providedActor: ProvidedArgument<StringArgument>,
        providedServer: ProvidedArgument<ServerArgument>,
        clientBotContext: ClientBotContext,
        client: Client
    ): String {
        var identifier = ""
        if (providedActor.isPresent()) {
            identifier = providedActor.getArgument().value
        }
        if (identifier.isEmpty()) {
            val persistedIdentifier =
                userServerIdentifierService
                    .getUserServerIdentifier(
                        clientBotContext.userId,
                        clientBotContext.chatId,
                        if (providedServer.isPresent()) providedServer.getArgument().value else throw NoServerProvidedException(),
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