package osu.salat23.circler.bot.response.templates


enum class TemplateType(val filename: String, val hasHtml: Boolean = false) {
    Profile("profile", true),
    Scores("scores", false)
//    Score("score", true),
//    ScoreCompact("score-compact"),
}