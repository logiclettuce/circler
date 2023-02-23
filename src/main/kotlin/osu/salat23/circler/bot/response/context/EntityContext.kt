package osu.salat23.circler.bot.response.context

import com.github.holgerbrandl.jsonbuilder.json
import org.json.JSONObject
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.osu.domain.*
import osu.salat23.circler.roundTo
import osu.salat23.circler.roundTo
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
            "score" to score.score
            "performance" to score.performance.roundTo(2)
            "performanceIdeal" to score.performanceIdeal.roundTo(2)
            "performancePerfect" to score.performancePerfect.roundTo(2)
            "accuracy" to (score.accuracy*100).roundTo(2)
            "achieved_combo" to score.maxCombo
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
            "beatmap" to beatmapJson(score.beatmap)
        }
    }

    fun beatmapJson(
        beatmap: Beatmap
    ): JSONObject {
        return json {
            "id" to beatmap.id
            "approach_rate" to beatmap.approachRate.roundTo(2)
            "circle_size" to beatmap.circleSize.roundTo(2)
            "hp_drain" to beatmap.hpDrain.roundTo(2)
            "circle_count" to beatmap.circleCount
            "slider_count" to beatmap.sliderCount
            "spinner_count" to beatmap.spinnerCount
            "max_combo" to beatmap.maxCombo
            "difficulty_rating" to beatmap.difficultyRating.roundTo(2)
            "aim_difficulty" to beatmap.aimDifficulty.roundTo(2)
            "speed_difficulty" to beatmap.speedDifficulty.roundTo(2)
            "speed_note_count" to beatmap.speedNoteCount.roundTo(2)
            "slider_factor" to beatmap.sliderFactor
            "overall_difficulty" to beatmap.overallDifficulty.roundTo(2)
            "flashlight_difficulty" to beatmap.flashlightDifficulty.roundTo(2)
            "mode" to beatmap.mode.displayName
            "status" to beatmap.status
            "url" to beatmap.url
            "version" to beatmap.version
            if (beatmap.beatmapSet != null)
                "beatmap_set" to beatmapSetJson(beatmap.beatmapSet)
        }
    }

    fun beatmapSetJson(
        beatmapSet: BeatmapSet
    ): JSONObject {
        return json {
            "id" to beatmapSet.id
            "title" to beatmapSet.title
            "artist" to beatmapSet.artist
            "creator" to beatmapSet.creator
            "cover_url" to beatmapSet.coverUrl
            "status" to beatmapSet.status
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