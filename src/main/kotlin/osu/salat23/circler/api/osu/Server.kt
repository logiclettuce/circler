package osu.salat23.circler.api.osu

enum class Server(
    val identifiers: Array<String>,
    val displayName: String,
    val beatmapsetUrl: Regex) {

    // todo either remove config files completely or make servers configurable
    Bancho(
        arrayOf(
            "b", "s", "bancho", "standard", "std",
            "и", "ы", "ифтсрщ", "ыефтвфкв", "ыев"
        ),
        "Bancho",
        Regex("https?:\\/\\/osu.ppy.sh\\/beatmapsets\\/[\\d]+(#[\\w]+)?\\/[\\d]+")
    ),
}