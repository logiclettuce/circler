package osu.salat23.circler.osu

import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.bot.command.commands.FetchUserProfileCommand
import osu.salat23.circler.bot.command.commands.FetchUserTopScoresCommand
import osu.salat23.circler.osu.domain.Beatmap
import osu.salat23.circler.osu.domain.Score
import osu.salat23.circler.osu.domain.User
import osu.salat23.circler.utility.Time
import java.text.DecimalFormat


object ResponseTemplates {
    private const val USER_LINK_TEMPLATE = "https://osu.ppy.sh/u/"

    fun osuUserTemplate(user: User, command: FetchUserProfileCommand): String {
        var playstyle = ""
        var highestRank = ""

//        if (user.playstyle != null) { todo: implement user playstyle
//            // ladno pohui p.s. it should not work like that
//            playstyle = user.playstyle.reduce { acc: String, s: String -> "${acc.capitalize()} ${s.capitalize()}" }
//            playstyle.trim()
//        }
        if (user.highestRank != 0L) {
            highestRank = """🔥(Highest: #${user.highestRank} at ${
                user.highestRankDate
            })"""
        }
// todo refactor this
        return """
    [Server: ${command.serverArgument.getArgument().value}]
    [Mode: ${user.playMode}]
    👤Player: ${user.username} ${if (user.isOnline) """🟢""" else ""}
    🌐: ${user.country.name}
    💹PP: ${user.performance}
    🏆Rank: #${user.globalRank} (${user.country.code} #${user.countryRank}) ${if (highestRank.isNotEmpty()) highestRank else "\n"}
    🎯Accuracy: ${user.accuracy}%
            """.trimIndent() + (if (playstyle.isNotEmpty()) "\nPlays with: ${playstyle}\n" else "\n") + """
    Playcount: ${user.playCount}
    Playtime: ${user.let { Time.fromSecondsToHMS(user.playTime) }} (Lv${user.level})
    
    🤙🏻 ${USER_LINK_TEMPLATE + user.id}
            """.trimIndent()
    }

    fun osuUserTopScores(user: User, command: FetchUserTopScoresCommand, scores: Array<Score>): String {
        val decimalFormat = DecimalFormat("#.##") // todo second decimal format for difficulty attributes
        val headerString = """
            [Server: ${command.serverArgument.getArgument().value}]
            [Mode: ${user.playMode}]
            Top scores for ${user.username}:${"\n"}
            
        """.trimIndent()
        val stringBuilder = StringBuilder()
        scores.forEach {
            stringBuilder.append(
                """
                    
                    Beatmap: ${it.beatmap.beatmapSet?.artist} - ${it.beatmap.beatmapSet?.title}
                    Difficulty: ${decimalFormat.format(it.beatmap.difficultyRating)}⭐ AR: ${decimalFormat.format(it.beatmap.approachRate)} CS: ${decimalFormat.format(it.beatmap.circleSize)} HP: ${decimalFormat.format(it.beatmap.hpDrain)} OD: ${decimalFormat.format(it.beatmap.overallDifficulty)}
                    Score: ${it.score}
                    PP: ${decimalFormat.format(it.performance)}
                """.trimIndent()
            )
            stringBuilder.append("\n")
        }
        return headerString + stringBuilder.toString().trim()
    }

    fun chatLeaderboard(users: List<User>): String {
        var result = ""

        for (user in users) {
            result+="""
                ${user.username} - ${user.performance}pp
            """.trimIndent()
            result+="\n"
        }
        return result.trimIndent()
    }

    fun chatBeatmapLeaderboard(beatmap: Beatmap, userScorePairs: List<Pair<User, Score>>): String {
        var scoresList = ""
        userScorePairs.forEach { pair ->
            scoresList += "${pair.first.username} - ${pair.second.score} | ${pair.second.performance}pp | ${pair.second.accuracy}% | x${pair.second.hitOk} 100s | x${pair.second.hitMeh} 50s | x${pair.second.hitMiss} miss\n"
        }
        scoresList = scoresList.trimIndent()



        var result = """
            ${beatmap.beatmapSet?.title ?: "???"} - ${beatmap.beatmapSet?.artist ?: "???"}
            
            $scoresList
        """.trimIndent()

        return result
    }

    fun osuUserRecentScores(user: User, command: Command, scores: Array<Score>): String {
        val stringBuilder = StringBuilder()
        scores.forEach {
            stringBuilder.append("""${it.beatmap.beatmapSet?.title} - ${it.beatmap.beatmapSet?.artist} | ${it.beatmap.difficultyRating}⭐ | ${it.performance}pp""")
            stringBuilder.append("\n")
        }
        return """
            $stringBuilder
        """.trimIndent()
    }

    fun beatmapInfo(beatmap: Beatmap): String {
        if (beatmap.beatmapSet == null) return couldNotExtractBeatmapInfo()
        return """
            ${beatmap.beatmapSet.artist} - ${beatmap.beatmapSet.title}
            created by ${beatmap.beatmapSet.creator}
            BPM: ${beatmap.speedNoteCount} Difficulty: ${beatmap.difficultyRating}
        """.trimIndent()
    }

    fun couldNotExtractBeatmapInfo() = "Could not extract information about beatmap!"
    fun osuUserNotDefined() = "No nickname was defined!"
    fun osuUserNotFound(identifier: String) = "User $identifier was not found!"
}
