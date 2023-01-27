package osu.salat23.circler.osu.domain

enum class Mode(val alternativeName: String, val id: Long, val identifiers: List<String>) {
    Standard("osu", 0, listOf("osu", "щыг")),
    Mania("mania", 3, listOf("mania", "ьфтшф")),
    Taiko("taiko", 1, listOf("taiko", "ефшлщ")),
    CatchTheBeat("fruits", 2, listOf("fruits", "акгшеы")),
    Default("", -1, listOf());

    companion object {
        fun from(id: Long): Mode {
            return Standard
        }

        fun from(name: String): Mode {
            for (value in Mode.values()) {
                if (value.alternativeName == name) return value
            }
            return Standard
        }
    }
}