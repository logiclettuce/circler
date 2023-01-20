package osu.salat23.circler.bot.commands.arguments

class ProvidedArgument<T: Argument> private constructor(
    private val provided: T? = null,
    private val default: T? = null,
) {

    companion object {
        fun <A: Argument> empty(): ProvidedArgument<A> {
            return ProvidedArgument()
        }
        fun <A: Argument> of(provided: A): ProvidedArgument<A> {
            return ProvidedArgument(
                provided = provided,
                default = null
            )
        }
        fun <A: Argument> of(provided: A, default: A): ProvidedArgument<A> {
            return ProvidedArgument(
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