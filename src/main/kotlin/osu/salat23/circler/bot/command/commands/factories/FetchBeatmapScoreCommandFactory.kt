package osu.salat23.circler.bot.command.commands.factories

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.arguments.*
import osu.salat23.circler.bot.command.arguments.factories.*
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchBeatmapScoreCommand
import osu.salat23.circler.configuration.domain.CommandConfiguration
import osu.salat23.circler.api.osu.Server

@Component
class FetchBeatmapScoreCommandFactory(
    commandConfiguration: CommandConfiguration,
    val actorArgumentFactory: ActorArgumentFactory,
    val serverArgumentFactory: ServerArgumentFactory,
    val beatmapIdArgumentFactory: BeatmapIdArgumentFactory,
    val pageSizeArgumentFactory: PageSizeArgumentFactory,
    val pageNumberArgumentFactory: PageNumberArgumentFactory
): GenericCommandFactory(commandConfiguration) {
    override fun getCommandKey(): String = "fetchBeatmapScore"
    override fun create(input: String): Command {
        val beatmapIdArgument = beatmapIdArgumentFactory.create(input, true)
        val actorArgument = actorArgumentFactory.create(input)
        val serverArgument = serverArgumentFactory.create(input).withDefault(ServerArgument(Server.Bancho))
        val pageSizeArgument = pageSizeArgumentFactory.create(input)
        val pageNumberArgument = pageNumberArgumentFactory.create(input)

        return FetchBeatmapScoreCommand(
            beatmapIdArgument = beatmapIdArgument,
            actorArgument = actorArgument,
            serverArgument = serverArgument,
            pageSizeArgument = pageSizeArgument,
            pageNumberArgument = pageNumberArgument
        )
    }
}