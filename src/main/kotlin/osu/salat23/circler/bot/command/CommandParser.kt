package osu.salat23.circler.bot.command

interface CommandParser {

    fun parse(input: String): Any
}