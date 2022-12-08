package osu.salat23.circler.bot.commands

class WrongCommandFormatException(val command: String, override val message: String) : RuntimeException(message)