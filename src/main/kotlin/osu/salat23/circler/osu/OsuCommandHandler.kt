package osu.salat23.circler.osu

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.osu.handlers.ChainHandler

@Component
class OsuCommandHandler(
    private val handlers: Array<ChainHandler>
) {
    fun handle(command: Command, client: Client, clientBotContext: ClientBotContext) {
        handlers.forEach { handler -> handler.checkAndHandle(command, client, clientBotContext) }
    }
}