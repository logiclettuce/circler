package osu.salat23.circler.bot.command.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Default(
    val value: String
)
