package osu.salat23.circler.bot.commands

import org.springframework.stereotype.Component
import osu.salat23.circler.bot.commands.exceptions.NotABotCommandException
import osu.salat23.circler.bot.commands.factories.CommandFactory
import java.io.InputStream

@Component
class CommandParser(val commandFactories: List<CommandFactory>) {
    fun parse(input: String, file: InputStream? = null): Command {
        commandFactories.forEach {factory ->
            val result = factory.checkAndCreate(input)
            if (result.isPresent) return result.get()
        }
        throw NotABotCommandException()
    }
}