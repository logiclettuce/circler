package osu.salat23.circler

import java.util.*

fun String.capitalize(): String {
    val strCopy = this
    return strCopy.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}