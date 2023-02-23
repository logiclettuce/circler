package osu.salat23.circler.api.osu.bancho.dto

import osu.salat23.circler.osu.domain.*
import osu.salat23.circler.osu.formula.performance.PerformanceCalculator
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


object Converter {
    fun convertToBeatmapSet(
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

    fun convertToBeatmap(
        beatmap: BanchoBeatmap,
        beatmapAttributes: BanchoBeatmapAttributes,
        beatmapSet: BanchoBeatmapSet? = null
        ): Beatmap {
        return Beatmap(
            id = beatmap.id.toString(),

            circleCount = beatmap.countCircles,
            sliderCount = beatmap.countSliders,
            spinnerCount = beatmap.countSpinners,

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
            status = Status.from(beatmap.status ?: Status.Graveyard.alternativeName),
            url = beatmap.url,
            version = beatmap.version,
            beatmapSet = if (beatmapSet != null) convertToBeatmapSet(beatmapSet) else null
        )
    }

    fun convertToUser(
        user: OsuUser
    ): User {
        val joinDate = ZonedDateTime.parse(user.joinDate, DateTimeFormatter.ISO_DATE_TIME)
            .withZoneSameInstant(ZoneId.systemDefault())
        val highestRankDate = ZonedDateTime.parse(user.highestRank?.updatedAt, DateTimeFormatter.ISO_DATE_TIME)
            .withZoneSameInstant(ZoneId.systemDefault()) ?: ZonedDateTime.now() // todo fix npe why tf it happend idk

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

    fun convertToScore(
        score: BanchoScore,
        beatmap: Beatmap,
        performanceCalculator: PerformanceCalculator
    ): Score {
        val resultScore = Score(
            id = score.id,
            score = score.score,
            performance = score.pp,
            performanceIdeal = score.pp,
            performancePerfect = score.pp,
            accuracy = score.accuracy,
            maxCombo = score.maxCombo,
            date = LocalDateTime.parse(score.created_at, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(
                ZoneId.of("UTC"))),
            mode = Mode.from(score.mode),
            rank = Rank.valueOf(score.rank),
            globalRank = score.rankGlobal,
            countryRank = score.rankCountry,
            hitPerfect = score.statistics.count_300,
            hitOk = score.statistics.count_100,
            hitMeh = score.statistics.count_50,
            hitMiss = score.statistics.count_miss,
            mods = Mod.fromStringList(score.mods),
            beatmap = beatmap
        )

        resultScore.performance =
            performanceCalculator.calculate(
                score = resultScore,
                beatmap = beatmap
            )
        resultScore.performanceIdeal =
            performanceCalculator.calculate(
                score = resultScore,
                beatmap = beatmap,
                PerformanceCalculator.CalculationType.Ideal
            )
        resultScore.performancePerfect =
            performanceCalculator.calculate(
                score = resultScore,
                beatmap = beatmap,
                PerformanceCalculator.CalculationType.Perfect
            )

        return resultScore
    }

}