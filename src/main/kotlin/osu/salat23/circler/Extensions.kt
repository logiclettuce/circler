package osu.salat23.circler

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.*

fun String.capitalize(): String {
    val strCopy = this
    return strCopy.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun clamp(value: Double, min: Double, max: Double): Double {
    return Math.max(min, Math.min(value, max))
}

suspend fun <A, B> Iterable<A>.pmap(f: suspend (A) -> B): List<B> = coroutineScope {
    map { async { f(it) } }.awaitAll()
}