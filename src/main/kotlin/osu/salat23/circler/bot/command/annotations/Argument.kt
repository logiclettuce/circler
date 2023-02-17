package osu.salat23.circler.bot.command.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Argument(
    val name: String,
    val description: String,
    vararg val identifiers: String,
    val required: Boolean = false,
    val implicit: Boolean = false
)

//
//name = "Player"
//description = "Something written here"
//identifiers = listOf("nick", "nickname", "тшсл", "тшслтфьу")
//required = false
//implicit = true