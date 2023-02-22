package osu.salat23.circler.api.osu

import osu.salat23.circler.bot.command.annotations.ArgumentSerialized

enum class Server(
    val displayName: String,
    val beatmapsetUrl: Regex) {

    @ArgumentSerialized("b", "s", "bancho", "standard", "std",
        "и", "ы", "ифтсрщ", "ыефтвфкв", "ыев")
    Bancho(
        "Bancho",
        Regex("https?:\\/\\/osu.ppy.sh\\/beatmapsets\\/[\\d]+(#[\\w]+)?\\/[\\d]+")
    ),
}