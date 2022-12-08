package osu.salat23.circler.utility

import kotlin.math.min

object Time {
    fun fromSecondsToHMS(seconds: Long): String {
        val days = seconds / (3600 * 24)
        val hours = (seconds % (3600 * 24)) / 3600
        val minutes = ((seconds % (3600 * 24)) % 3600) / 60
        val secondsR = (((seconds % (3600 * 24)) % 3600) % 60)
        return "${days}d ${hours}h ${minutes}m ${secondsR}s"
    }
}
