package osu.salat23.circler.bot.response.context

import com.github.holgerbrandl.jsonbuilder.json
import org.json.JSONObject
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.osu.domain.Score
import osu.salat23.circler.osu.domain.User
import osu.salat23.circler.roundTo
import osu.salat23.circler.withDigits
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object EntityContext {

    fun userJson(
        user: User,
    ): JSONObject {
        return json {
                "id" to user.id
                "username" to user.username
                "is_online" to user.isOnline
                "has_supporter" to user.hasSupporter
                "mode" to user.playMode.displayName
                "avatar_url" to user.avatarUrl
                "cover_url" to user.coverUrl
                "country_name" to user.country.name
                "country_code" to user.country.code
                "join_date" to user.joinDate
                "performance" to user.performance
                "global_rank" to user.globalRank
                "country_rank" to user.countryRank
                "accuracy" to user.accuracy.roundTo(2)
                "level" to user.level
                "level_progress" to user.levelProgress
                "playcount" to user.playCount
                "playtime" to user.playTime
                "max_combo" to user.maximumCombo
                "ranked_score" to user.rankedScore
                "total_score" to user.totalScore
                "total_hits" to user.totalHits
                "highest_rank" to user.highestRank
                "highest_rank_date" to user.highestRankDate
        }
    }

    fun scoreJson(
        score: Score
    ): JSONObject {
        return json {
            "id" to score.id
            "score" to score.score.toString()
            "performance" to score.performance.withDigits(2)
            "accuracy" to score.accuracy.withDigits(2)
            "max_combo" to score.maxCombo
            "date" to score.date
            "mode" to score.mode.displayName
            "rank" to score.rank.name
            "global_rank" to score.globalRank
            "country_rank" to score.countryRank
            "hit_perfect" to score.hitPerfect
            "hit_ok" to score.hitOk
            "hit_meh" to score.hitMeh
            "hit_miss" to score.hitMiss
            "mods" to score.mods.sortedBy { it.id }.map { it.alternativeName }.toTypedArray()
        }
    }

    fun metaJson(
        server: Server
    ): JSONObject {
        return json {
            "server" to server.displayName
            "generated_date" to LocalDate.now()
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)).toString()
        }
    }
}