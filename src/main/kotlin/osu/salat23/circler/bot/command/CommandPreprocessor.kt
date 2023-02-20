package osu.salat23.circler.bot.command

interface CommandPreprocessor {
    fun process(input: String): String
}