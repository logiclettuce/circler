package osu.salat23.circler.bot.command.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Command(
    val name: String,
    val description: String,
    vararg val identifiers: String,
)
