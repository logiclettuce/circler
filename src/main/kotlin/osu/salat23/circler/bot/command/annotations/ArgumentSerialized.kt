package osu.salat23.circler.bot.command.annotations


@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class ArgumentSerialized(
    vararg val identifiers: String
)