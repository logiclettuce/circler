package osu.salat23.circler.bot.command.exceptions

class ArgumentIsNotDefinedException(argumentKey: String):
    RuntimeException("Argument with key: $argumentKey is not defined.")