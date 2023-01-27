package osu.salat23.circler.bot.command.arguments

class ProvidedArgument<T: osu.salat23.circler.bot.command.arguments.Argument> private constructor(
    private val provided: T? = null,
    private val default: T? = null,
) {

    companion object {
        fun <A: osu.salat23.circler.bot.command.arguments.Argument> empty(): osu.salat23.circler.bot.command.arguments.ProvidedArgument<A> {
            return osu.salat23.circler.bot.command.arguments.ProvidedArgument()
        }
        fun <A: osu.salat23.circler.bot.command.arguments.Argument> of(provided: A): osu.salat23.circler.bot.command.arguments.ProvidedArgument<A> {
            return osu.salat23.circler.bot.command.arguments.ProvidedArgument(
                provided = provided,
                default = null
            )
        }
        fun <A: osu.salat23.circler.bot.command.arguments.Argument> of(provided: A, default: A): osu.salat23.circler.bot.command.arguments.ProvidedArgument<A> {
            return osu.salat23.circler.bot.command.arguments.ProvidedArgument(
                provided = provided,
                default = default
            )
        }
    }
    fun isPresent(): Boolean {
        return provided != null || default != null
    }

    fun getArgument(): T {
        if (provided != null) return provided
        return default!!
    }

    fun withDefault(default: T): ProvidedArgument<T> {
        return ProvidedArgument(provided, default)
    }
}