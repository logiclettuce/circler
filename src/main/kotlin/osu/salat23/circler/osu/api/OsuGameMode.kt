package osu.salat23.circler.osu.api

enum class OsuGameMode(val value: String, val rulesetId: Int) {
    Standard("osu", 0),
    Taiko("taiko", 1),
    Catch("fruits", 2),
    Mania("mania", 3),
    UserDefault("", -1)
}