package osu.salat23.circler.bot.commands.exceptions

class CouldNotParseArgumentValueException(private val stringValue: String, private val typeName: String):
    RuntimeException("Could not parse value: '$stringValue' into $typeName")