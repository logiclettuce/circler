package osu.salat23.circler.osu.domain

enum class Mode(val alternativeName: String, val id: Long) {
    Standard("osu", 0),
    Mania("mania", 3),
    Taiko("taiko", 1),
    CatchTheBeat("fruits", 2),
    Default("", -1);

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