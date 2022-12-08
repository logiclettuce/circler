package osu.salat23.circler.osu.handlers

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import osu.salat23.circler.bot.UserContext
import osu.salat23.circler.bot.client.Client
import osu.salat23.circler.bot.client.ClientEntity
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.ResponseTemplates
import osu.salat23.circler.osu.api.OsuApi
import osu.salat23.circler.osu.api.OsuGameMode
import osu.salat23.circler.osu.api.domain.models.OsuBeatmapAttributes
import osu.salat23.circler.osu.api.domain.models.OsuScore
import osu.salat23.circler.service.OsuService
import java.util.Collections


@Component
class FetchUserTopScoresHandler(val osuService: OsuService) : ChainHandler() {

    private fun getScoresWithBeatmapAttributes(scores: Array<OsuScore>, osuApi: OsuApi): List<Pair<OsuScore, OsuBeatmapAttributes>> {
        val pairs = Collections.synchronizedList(mutableListOf<Pair<OsuScore, OsuBeatmapAttributes>>())
        runBlocking {
            for (score in scores) {
                launch {
                    val attributes = osuApi.beatmapAttributes(score.beatmap.id, 0, OsuGameMode.UserDefault)
                    pairs.add(Pair(score, attributes))
                }
            }
        }
        return pairs
    }

    override fun handleUpdate(command: Command, client: Client, userContext: UserContext) {
        var identifier = command.options.actor
        if (identifier.isEmpty()) identifier = "salat23"

        val osuApi = osuService.getOsuApiByServer(command.server)
        val user = osuApi.user(identifier)
        val scores = osuApi.userScores(identifier, OsuScore.Type.Best, command.options.pageSize, command.options.pageNumber)
            //val scoresWithBeatmapAttributes =  getScoresWithBeatmapAttributes(scores, osuApi)

        val text = ResponseTemplates.osuUserTopScores(user, command, scores)
        client.send(
            ClientEntity.Builder()
                .chatId(userContext.chatId)
                .userId(userContext.userId)
                .text(text)
                .build()
        )
    }

    override fun canHandle(command: Command, userContext: UserContext): Boolean {
        val canHandle = when (command.action) {
            Command.Action.USER_TOP_SCORES -> true
            else -> false
        }
        return canHandle
    }
}