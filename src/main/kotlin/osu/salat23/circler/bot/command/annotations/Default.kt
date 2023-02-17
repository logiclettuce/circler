package osu.salat23.circler.bot.command.annotations

import kotlin.reflect.KClass

@Target(
    AnnotationTarget.PROPERTY
)
annotation class Default(
    val value: String
)
