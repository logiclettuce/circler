package osu.salat23.circler.bot.command

class CommandParsingException(message: String, val type: CommandParsingErrorType): RuntimeException(message)