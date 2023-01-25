package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.factories.BeatmapIdArgumentFactory
import osu.salat23.circler.bot.command.commands.ChatLeaderboardCommand
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchBeatmapCommand
import osu.salat23.circler.bot.command.exceptions.CommandIsNotDefinedException
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.configuration.domain.Command as ConfigCommand

@Component
class FetchBeatmapIdCommandFactory(
    private val commandConfiguration: CommandConfiguration,
    val beatmapIdCommandFactory: BeatmapIdArgumentFactory
): CommandFactory() {
    private final val configuration: ConfigCommand
    companion object {
        private const val COMMAND_KEY = "fetchBeatmap"
    }
    init {
        if (!commandConfiguration.commands.containsKey(COMMAND_KEY))
            throw CommandIsNotDefinedException(COMMAND_KEY)
        configuration = commandConfiguration.commands[COMMAND_KEY]!!
    }
    override fun create(input: String): Command {
        var input = input

        // remove the action part
        for (actionIdentifier in configuration.identifiers) {
            if (input.startsWith("$actionIdentifier ")) {
                input  = input.substring(actionIdentifier.length+1)
                break
            }
        }

        val beatmapIdArgument = beatmapIdCommandFactory.create(input, true)

        return FetchBeatmapCommand(
            beatmapIdArgument = beatmapIdArgument
        )
    }

    override fun canCreate(input: String): Boolean {
        return CommandFactoryHelpers.canCreateBasic(input, COMMAND_KEY, commandConfiguration)
    }
}