package osu.salat23.circler.osu.handlers

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientMessage
import osu.salat23.circler.bot.commands.Command

@Component
class HelpHandler : ChainHandler() {
    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {

        var servers: String = ""
        var serverSpecificCommands: String = ""
        var nonServerSpecificCommands: String = ""

        for (server in Command.Server.values().filter { server -> server != Command.Server.None }) {
            var serverIdentifiers = ""
            for (identifier in server.identifiers) {
                serverIdentifiers += "${identifier}, "
            }
            serverIdentifiers = serverIdentifiers.trimIndent()

            servers += "${server.displayName} - ($serverIdentifiers)\n"
        }

        for (action in Command.Action.values().filter { action -> !action.nonServerSpecific }) {
            var actionIdentifiers = ""
            for (identifier in action.identifiers) {
                actionIdentifiers += "${identifier}, "
            }
            actionIdentifiers = actionIdentifiers.trimIndent()

            serverSpecificCommands += "${action.displayedName} - ($actionIdentifiers)\n"
        }

        for (action in Command.Action.values().filter { action -> action.nonServerSpecific }) {
            var actionIdentifiers = ""
            for (identifier in action.identifiers) {
                actionIdentifiers += "${identifier}, "
            }
            actionIdentifiers = actionIdentifiers.trimIndent()

            nonServerSpecificCommands += "${action.displayedName} - ($actionIdentifiers)\n"
        }

        servers = servers.trimIndent()
        serverSpecificCommands = serverSpecificCommands.trimIndent()
        nonServerSpecificCommands = nonServerSpecificCommands.trimIndent()


        val commandsDescription = """
            () - optional
            <> - placeholder
            
            To use server specific command - <server> <action> (<implicit parameter name>) -<parameter name> <parameter value>
            
            Available servers:
            $servers
            
            Available server specific actions:
            $serverSpecificCommands
            
            
            To use non-server specific command - <action> (<implicit parameter name>) -<parameter name> <parameter value>
            
            Available non-server specific actions:
            $nonServerSpecificCommands
            
            For further information contact @salat23
        """.trimIndent()


        client.send(
            ClientMessage(
                chatId = userContext.chatId,
                userId = userContext.userId,
                text = commandsDescription
            )
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        return (command.action == Command.Action.HELP)
    }
}