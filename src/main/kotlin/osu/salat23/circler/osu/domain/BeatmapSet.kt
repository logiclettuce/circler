package osu.salat23.circler.osu.domain

data class BeatmapSet(
    val id: String,
    val title: String,
    val artist: String,
    val creator: String,
    val coverUrl: String,
    val status: Status,
    val beatmaps: Array<Beatmap>
)