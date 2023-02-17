package osu.salat23.circler.bot.command

enum class CommandParsingErrorType {
    CommandNotPresent,
    OddAmountOfQuotes,
    CommandNotFound,
    ArgumentWrongNameFormat,
    NoSuchArgumentName,
    CouldNotObtainArgumentField,
    ArgumentValueTypeIsWrong,
    CouldNotGetDefaultArgumentValue,
    ArgumentValueIsNotProvided, // when argument is present in command but no value provided (excl boolean)
    NoSuchValueInEnum
}