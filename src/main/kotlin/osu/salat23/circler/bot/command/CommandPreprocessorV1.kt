package osu.salat23.circler.bot.command

import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.command.arguments.BeatmapIdArgument
import osu.salat23.circler.bot.command.arguments.ProvidedArgument

@Component
class CommandPreprocessorV1(

): CommandPreprocessor {

    override fun preprocess(input: String): String {


        for (server in Server.values()) {
            val serverBeatmapUrlMatches = server.beatmapsetUrl.findAll(input)
            val iterator = serverBeatmapUrlMatches.iterator()
            while(iterator.hasNext()) {
                val matchedResult = iterator.next()
                val url = matchedResult.value

            }
        }
    }
}