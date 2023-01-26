package osu.salat23.circler.bot.command.exceptions

class WrongCommandFormatException(val command: String, override val message: String) : RuntimeException(message)