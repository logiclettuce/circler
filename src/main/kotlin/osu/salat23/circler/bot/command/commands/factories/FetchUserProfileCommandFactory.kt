package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.factories.ServerArgumentFactory
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchUserProfileCommand
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.command.arguments.*
import osu.salat23.circler.bot.command.arguments.factories.GameModeArgumentFactory
import osu.salat23.circler.bot.command.arguments.factories.DoRenderArgumentFactory
import osu.salat23.circler.osu.domain.Mode

@Component
class FetchUserProfileCommandFactory(
    commandConfiguration: CommandConfiguration,
    private val actorArgumentFactory: osu.salat23.circler.bot.command.arguments.factories.ActorArgumentFactory,
    private val serverArgumentFactory: ServerArgumentFactory,
    private val gameModeArgumentFactory: GameModeArgumentFactory,
    private val doRenderArgumentFactory: DoRenderArgumentFactory
): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "fetchUserProfile"
    override fun create(input: String): Command {
        val actorArgument = actorArgumentFactory.create(input, true)
        val serverArgument = serverArgumentFactory.create(input).withDefault(
            ServerArgument(
                Server.Bancho
            )
        )
        val gameModeArgument = gameModeArgumentFactory.create(input)
        val isHtmlArgument = doRenderArgumentFactory.create(input)

        return FetchUserProfileCommand(
            actorArgument = actorArgument,
            serverArgument = serverArgument,
            gameModeArgument = gameModeArgument,
            isHtmlArgument = isHtmlArgument
        )
    }

    fun produceCall(actor: String, server: Server, mode: Mode, isHtml: Boolean): String {
        var commandText = ""
        val commandActionIdentifiers = commandConfiguration.commands[getCommandKey()]!!.identifiers
        commandText += "${commandActionIdentifiers[0]} "

        commandText += actorArgumentFactory.produceCall(StringArgument(actor)) + " "
        commandText += doRenderArgumentFactory.produceCall(BooleanArgument(isHtml)) + " "
        commandText += gameModeArgumentFactory.produceCall(GameModeArgument(mode)) + " "
        commandText += serverArgumentFactory.produceCall(ServerArgument(server)) + " "

        return commandText
    }

}