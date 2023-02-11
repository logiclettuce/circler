package osu.salat23.circler

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.text.DecimalFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

fun String.capitalize(): String {
    val strCopy = this
    return strCopy.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun clamp(value: Double, min: Double, max: Double): Double {
    return Math.max(min, Math.min(value, max))
}

infix fun String.mul(times: Int): String {
    val strList = mutableListOf<String>()
    for (i in 1..times) {
        strList.add(this)
    }
    return strList.reduce{acc: String, s: String -> return acc+s }
}
fun Double.withDigits(amount: Int): String {
    if (amount == 0) {
        val df = DecimalFormat("#")
        return df.format(this)
    }
    val df = DecimalFormat("#.${"#" mul amount}")
    return df.format(this)
}

fun Double.roundTo(numFractionDigits: Int): Double {
    val factor = 10.0.pow(numFractionDigits.toDouble())
    return (this * factor).roundToInt() / factor
}