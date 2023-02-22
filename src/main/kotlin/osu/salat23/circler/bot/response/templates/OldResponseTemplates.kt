package osu.salat23.circler.bot.response.templates

import osu.salat23.circler.bot.command.impl.UserProfileCommand
import osu.salat23.circler.bot.command.impl.FetchUserTopScoresCommand
import osu.salat23.circler.osu.domain.Beatmap
import osu.salat23.circler.osu.domain.Mode
import osu.salat23.circler.osu.domain.Score
import osu.salat23.circler.osu.domain.User
import osu.salat23.circler.utility.Time
import java.text.DecimalFormat


object OldResponseTemplates {
    private const val USER_LINK_TEMPLATE = "https://osu.ppy.sh/u/"

    fun osuUserTemplate(user: User, command: UserProfileCommand, gameMode: Mode): String {
        val playstyle = ""
        var highestRank = ""

        if (user.highestRank != 0L) {
            highestRank = """🔥(Highest: #${user.highestRank} at ${
                user.highestRankDate
            })"""
        }

        return """
    [Server: ${command.server.displayName}]
    [Mode: ${if (gameMode == Mode.Default) user.playMode.name else gameMode.name}]
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

    fun osuUserTopScores(user: User, command: FetchUserTopScoresCommand, scores: List<Score>): String {
        val decimalFormat = DecimalFormat("#.##")
        val headerString = """
            [Server: ${command.server.displayName}]
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



        val result = """
            ${beatmap.beatmapSet?.title ?: "???"} - ${beatmap.beatmapSet?.artist ?: "???"}
            
            $scoresList
        """.trimIndent()

        return result
    }

    fun osuUserRecentScores(user: User, scores: List<Score>): String {
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
