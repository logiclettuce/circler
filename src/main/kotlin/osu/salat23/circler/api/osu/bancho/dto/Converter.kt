package osu.salat23.circler.api.osu.bancho.dto

import osu.salat23.circler.osu.domain.*
import osu.salat23.circler.osu.formula.performance.PerformanceCalculator
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


object Converter {

    fun convert(
        beatmapSet: BanchoBeatmapSet,
        beatmaps: Array<Beatmap> = arrayOf()
    ): BeatmapSet {
        return BeatmapSet(
            id = beatmapSet.id,
            title = beatmapSet.title,
            artist = beatmapSet.artist,
            creator = beatmapSet.creator,
            coverUrl = beatmapSet.covers.cover ?: "",
            status = Status.from(beatmapSet.status),
            beatmaps = beatmaps
        )
    }

    fun convert(
        beatmap: BanchoBeatmap,
        beatmapAttributes: BanchoBeatmapAttributes,
        beatmapSet: BanchoBeatmapSet? = null
        ): Beatmap {
        return Beatmap(
            id = beatmap.id.toString(),

            circleCount = beatmap.count_circles,
            sliderCount = beatmap.count_sliders,
            spinnerCount = beatmap.count_spinners,

            approachRate = beatmapAttributes.approachRate,
            circleSize = beatmap.cs,
            hpDrain = beatmap.drain,
            overallDifficulty = beatmapAttributes.overallDifficulty,

            maxCombo = beatmapAttributes.max_combo,
            difficultyRating = beatmapAttributes.starRating,
            aimDifficulty = beatmapAttributes.aimDifficulty,
            speedDifficulty = beatmapAttributes.speedDifficulty,
            speedNoteCount = beatmapAttributes.speedNoteCount,
            sliderFactor = beatmapAttributes.sliderFactor,
            flashlightDifficulty = beatmapAttributes.flashlightDifficulty,

            mode = Mode.from(beatmap.mode),
            status = Status.from(beatmap.status),
            url = beatmap.url,
            beatmapSet = if (beatmapSet != null) convert(beatmapSet) else null
        )
    }

    fun convert(
        user: OsuUser
    ): User {
        val joinDate = ZonedDateTime.parse(user.joinDate, DateTimeFormatter.ISO_DATE_TIME)
            .withZoneSameInstant(ZoneId.systemDefault())
        val highestRankDate = ZonedDateTime.parse(user.highestRank?.updatedAt, DateTimeFormatter.ISO_DATE_TIME)
            .withZoneSameInstant(ZoneId.systemDefault()) ?: ZonedDateTime.now()

        return User(
            id =  user.id,
            username = user.username,
            isOnline = user.isOnline,
            hasSupporter = user.hasSupporter,
            avatarUrl = user.avatarUrl,
            coverUrl = user.coverUrl,
            country = Country(name = user.country.name, code = user.country.code),
            joinDate = joinDate.toLocalDate(),
            playMode = Mode.from(user.playmode),
            performance = if (user.statistics != null) user.statistics.pp else 0,
            globalRank = if (user.statistics != null) user.statistics.globalRank else 0,
            countryRank = if (user.statistics != null) user.statistics.countryRank else 0,
            accuracy = if (user.statistics != null) user.statistics.hitAccuracy else .0,
            level = if (user.statistics?.level != null) user.statistics.level.current else 0,
            levelProgress = if (user.statistics?.level != null) user.statistics.level.progress else 0,
            playCount = if (user.statistics != null) user.statistics.playCount else 0,
            playTime = if (user.statistics != null) user.statistics.playTime else 0,
            maximumCombo = if (user.statistics != null) user.statistics.maximumCombo else 0,
            rankedScore = if (user.statistics != null) user.statistics.rankedScore else 0,
            totalScore = if (user.statistics != null) user.statistics.totalScore else 0,
            totalHits = if (user.statistics != null) user.statistics.totalHits else 0,
            highestRank = if (user.highestRank != null) user.highestRank.rank else 0,
            highestRankDate = highestRankDate.toLocalDate(),
        )
    }

    fun convert(
        score: OsuScore,
        beatmap: Beatmap,
        performanceCalculator: PerformanceCalculator
    ): Score {
        val resultScore = Score(
            id = score.id,
            score = score.score,
            performance = score.pp,
            accuracy = score.accuracy,
            maxCombo = score.maxCombo,
            date = LocalDate.now() /*.parse(score.created_at)*/,
            mode = Mode.from(score.mode),
            rank = Rank.valueOf(score.rank),
            globalRank = score.rankGlobal,
            countryRank = score.rankCountry,
            hitPerfect = score.statistics.count_300,
            hitOk = score.statistics.count_100,
            hitMeh = score.statistics.count_50,
            hitMiss = score.statistics.count_miss,
            mods = Mod.fromStringArray(score.mods),
            beatmap = beatmap
        )

        resultScore.performance =
            performanceCalculator.calculate(
                score = resultScore,
                beatmap = beatmap
            )

        return resultScore
    }

}