package osu.salat23.circler.bot.command.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Argument(
    val name: String,
    val description: String,
    vararg val identifiers: String,
    val required: Boolean = false,
    val implicit: Boolean = false
)