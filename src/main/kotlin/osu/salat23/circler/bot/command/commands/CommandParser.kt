package osu.salat23.circler.bot.command.commands

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.command.commands.factories.CommandFactory
import osu.salat23.circler.bot.command.exceptions.NotABotCommandException
import java.io.InputStream

@Component
class CommandParser(val commandFactories: List<CommandFactory>) {
    fun parse(input: String): Command {
        commandFactories.forEach {factory ->
            val result = factory.checkAndCreate(input)
            if (result.isPresent) return result.get()
        }
        throw NotABotCommandException()
    }
}