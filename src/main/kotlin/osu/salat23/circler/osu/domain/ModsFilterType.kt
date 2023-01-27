package osu.salat23.circler.osu.domain

enum class ModsFilterType(val prefix: String) {
    Inclusive("+"),
    PartiallyInclusive("-"),
    Strict("=")
}