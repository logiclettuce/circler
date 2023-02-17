package osu.salat23.circler.bot.command

interface CommandPreprocessor {
    fun preprocess(input: String): String
}