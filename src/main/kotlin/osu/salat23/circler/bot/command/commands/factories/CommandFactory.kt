package osu.salat23.circler.bot.command.commands.factories

import osu.salat23.circler.bot.command.commands.Command
import java.util.*

abstract class CommandFactory {
    protected abstract fun create(input: String): Command

    protected abstract fun canCreate(input: String): Boolean

    open fun checkAndCreate(input: String): Optional<Command> {
        if (canCreate(input)) return Optional.of(create(input))
        return Optional.empty()
    }
}