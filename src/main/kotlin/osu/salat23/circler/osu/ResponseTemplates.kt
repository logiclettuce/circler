package osu.salat23.circler.osu

import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.api.domain.models.OsuUser
import osu.salat23.circler.capitalize
import osu.salat23.circler.osu.api.domain.models.OsuBeatmapAttributes
import osu.salat23.circler.osu.api.domain.models.OsuScore
import osu.salat23.circler.utility.Time
import java.time.ZonedDateTime


object ResponseTemplates {
    private const val USER_LINK_TEMPLATE = "https://osu.ppy.sh/u/"

    fun osuUserTemplate(user: OsuUser, command: Command): String {
        var playstyle = ""
        var highestRank = ""

        if (user.playstyle != null) {
            // ladno pohui p.s. it should not work like that
            playstyle = user.playstyle.reduce { acc: String, s: String -> "${acc.capitalize()} ${s.capitalize()}" }
            playstyle.trim()
        }
        if (user.highestRank != null) {
            highestRank = """🔥(Highest: #${user.highestRank.rank} at ${
                ZonedDateTime.parse(
                    user.highestRank.updatedAt
                ).toLocalDate()
            })"""
        }

        return """
    [Server: ${command.server.name}]
    [Mode: ${user.playmode}]
    👤Player: ${user.username} ${if (user.isOnline) """🟢""" else ""}
    🌐: ${user.country.name}
    💹PP: ${user.statistics?.pp}
    🏆Rank: #${user.statistics?.globalRank} (${user.country.code} #${user.statistics?.countryRank}) ${if (highestRank.isNotEmpty()) highestRank else "\n"}
    🎯Accuracy: ${user.statistics?.hitAccuracy}%
            """.trimIndent() + (if (playstyle.isNotEmpty()) "\nPlays with: ${playstyle}\n" else "\n") + """
    Playcount: ${user.statistics?.playCount}
    Playtime: ${user.statistics?.let { Time.fromSecondsToHMS(user.statistics.playTime) }} (Lv${user.statistics?.level?.current})
    
    🤙🏻 ${USER_LINK_TEMPLATE + user.id}
            """.trimIndent()
    }

    fun osuUserTopScores(user: OsuUser, command: Command, scores: Array<OsuScore>): String {
        val headerString = """
            [Server: ${command.server.name}]
            [Mode: ${user.playmode}]
            Top scores for ${user.username}:${"\n"}
            
        """.trimIndent()
        val stringBuilder = StringBuilder()
        scores.forEach {
            stringBuilder.append(
                """
                    
                    Beatmap: ${it.beatmapset.artist} - ${it.beatmapset.title}
                    Difficulty: ${it.beatmap.difficulty_rating}⭐ AR: ${it.beatmap.ar} CS: ${it.beatmap.cs} HP: ${it.beatmap.drain}
                    Result: ${it.score}
                    PP: ${it.pp.toInt()}
                """.trimIndent()
            ) // todo make od somehow?
            stringBuilder.append("\n")
        }
        return headerString + stringBuilder.toString().trim()
    }

    fun osuUserRecentScores(user: OsuUser, command: Command, scores: Array<OsuScore>): String {
        val stringBuilder = StringBuilder()
        scores.forEach {
            stringBuilder.append("""${it.beatmapset.title} - ${it.beatmapset.artist} | ${it.beatmap.difficulty_rating}⭐ | ${it.pp.toInt()}pp""")
            stringBuilder.append("\n")
        }
        return """
            $stringBuilder
        """.trimIndent()
    }

    fun osuUserNotFound(identifier: String) = "User $identifier was not found"
}
