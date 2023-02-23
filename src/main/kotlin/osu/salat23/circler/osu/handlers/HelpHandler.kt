package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.ClientBotContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.command.CommandContext
import osu.salat23.circler.bot.command.impl.HelpCommand

@Component
class HelpHandler(
    val commandContext: CommandContext
) : ChainHandler() {
    override fun handleUpdate(command: Any, client: Client, clientBotContext: ClientBotContext) {
        val command = command as HelpCommand

        if (command.commandIdentifier.isEmpty()) client.send(allCommandsInfo(clientBotContext))
        else client.send(commandInfo(command.commandIdentifier, clientBotContext))
    }

    private fun allCommandsInfo(cbc: ClientBotContext): ClientEntity {
        val commandsTextSb = StringBuilder("Available commands:\n")
        for (commandInfo in commandContext.commands) {
            val identifiersSb = StringBuilder("Identifiers: ")
            commandInfo.metadata.identifiers.joinTo(identifiersSb, separator = ", ", prefix = "[", postfix = "]")
            val identifiersText = identifiersSb.toString()
            val row = """
                $identifiersText
                ${commandInfo.metadata.name} (${commandInfo.primaryIdentifier}) - ${commandInfo.metadata.description}
            """.trimIndent()
            commandsTextSb.appendLine(row)
            commandsTextSb.appendLine()
        }
        return ClientMessage(
            userId = cbc.userId,
            chatId = cbc.chatId,
            text = commandsTextSb.toString()
        )
    }

    private fun commandInfo(commandIdentifier: String, cbc: ClientBotContext): ClientEntity {
        val commandInfo = commandContext.identifierToCommandInfo[commandIdentifier] ?: return ClientMessage(
            userId = cbc.userId,
            chatId = cbc.chatId,
            text = "Specified command does not exist. Use command identifiers to specify a command."
        )
            // todo replace hardcode with context
        val identifiersText = commandInfo.metadata.identifiers.joinToString(", ", prefix = "(", postfix = ")")
        val argumentsTextSb = StringBuilder()
        commandInfo.arguments.forEach { argumentInfo ->
            val argumentIdentifiersText = argumentInfo.metadata.identifiers.joinToString(", ", prefix = "(", postfix = ")")
            val argumentText = """
                    ${if (argumentInfo.metadata.implicit) "(implicit)" else ""}${if (argumentInfo.metadata.required) "(required)" else ""} ${argumentInfo.metadata.name} - ${argumentInfo.metadata.description}
                    $argumentIdentifiersText
                """.trimIndent()
            argumentsTextSb.appendLine(argumentText)
        }

        val commandDescriptionText = """
            ${commandInfo.metadata.name}
            ---
            ${commandInfo.metadata.description}
            ---
            Called with: $identifiersText
            ---
            Available arguments:
            
            $argumentsTextSb
        """.trimIndent()

        return ClientMessage(
            userId = cbc.userId,
            chatId = cbc.chatId,
            text = commandDescriptionText
        )
    }

    override fun canHandle(command: Any, clientBotContext: ClientBotContext): Boolean {
        return command is HelpCommand
    }
}