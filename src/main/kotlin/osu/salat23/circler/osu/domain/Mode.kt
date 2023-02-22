package osu.salat23.circler.osu.domain

import osu.salat23.circler.bot.command.annotations.ArgumentSerialized

enum class Mode(val alternativeName: String, val displayName: String, val id: Long) {
    @ArgumentSerialized("standard", "standart", "std", "osu", "s",
                                    "ыефтвфкв", "ыефтвфке", "ыев", "щыг", "ы")
    Standard("osu", "Standard",0),

    @ArgumentSerialized("mania", "osumania", "m",
                                    "ьфтшф", "щыгьфтшф", "ь")
    Mania("mania", "Mania",3),

    @ArgumentSerialized("taiko", "osutaiko", "t",
                                    "ефшлщ", "щыгефшлщ", "е")
    Taiko("taiko", "Taiko",1),

    @ArgumentSerialized("catchthebeat", "fruits", "osucatchthebeat", "ctb", "c",
                                    "сфесреруиуфе", "акгшеы", "щыгсфесреруиуфе", "сеи", "с",)
    CatchTheBeat("fruits", "Catch the beat",2),

    @ArgumentSerialized("default", "def", "d",
                                    "вуафгде", "вуа", "в")
    Default("", "",-1);

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