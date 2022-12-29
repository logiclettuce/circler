package osu.salat23.circler.api.osu.bancho.dto

import osu.salat23.circler.osu.domain.Beatmap
import osu.salat23.circler.osu.domain.BeatmapSet
import osu.salat23.circler.osu.domain.Mode
import osu.salat23.circler.osu.domain.Status

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

}