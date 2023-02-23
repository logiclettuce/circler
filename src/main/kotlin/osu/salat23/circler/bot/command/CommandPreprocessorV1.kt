package osu.salat23.circler.bot.command

import org.springframework.stereotype.Component
import osu.salat23.circler.api.osu.Server

@Component
class CommandPreprocessorV1(

): CommandPreprocessor {

    override fun process(input: String): String {
        val partsToReplace = mutableListOf<Pair<String, IntRange>>()
        for (server in Server.values()) {
            val serverBeatmapUrlMatches = server.beatmapsetUrl.findAll(input)
            val iterator = serverBeatmapUrlMatches.iterator()
            while(iterator.hasNext()) {
                val matchedResult = iterator.next()
                val url = matchedResult.value

                val reversedUrl = url.reversed()
                var value = ""
                for (char in reversedUrl) {
                    if (char == '/') break
                    value += char
                }

                partsToReplace.add(value.reversed() to matchedResult.range)
            }
        }

        var result = input
        for (part in partsToReplace.reversed()) {
            result = result.replaceRange(part.second, part.first)
        }

        return result
    }
}