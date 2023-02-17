package osu.salat23.circler.bot.command

enum class CommandParsingErrorType {
    CommandNotPresent,
    OddAmountOfQuotes,
    CommandNotFound,
    ArgumentWrongNameFormat,
    NoSuchArgumentName,
    CouldNotObtainArgumentField,
    ArgumentValueTypeIsWrong,
    CouldNotGetDefaultArgumentValue
}