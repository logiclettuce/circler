package osu.salat23.circler.bot.commands.exceptions

class CommandIsNotDefinedException(commandKey: String):
    RuntimeException("Command with key: $commandKey is not defined.") {
}