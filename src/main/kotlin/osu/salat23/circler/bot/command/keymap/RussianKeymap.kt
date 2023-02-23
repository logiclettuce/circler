package osu.salat23.circler.bot.command.keymap

import org.springframework.stereotype.Component

@Component
class RussianKeymap: LayoutKeymap {
    override fun getKeymap(): Map<String, String> = mapOf(
        "q" to "й",
        "w" to "ц",
        "e" to "у",
        "r" to "к",
        "t" to "е",
        "y" to "н",
        "u" to "г",
        "i" to "ш",
        "o" to "щ",
        "p" to "з",
        "[" to "х",
        "]" to "ъ",
        "a" to "ф",
        "s" to "ы",
        "d" to "в",
        "f" to "а",
        "g" to "п",
        "h" to "р",
        "j" to "о",
        "k" to "л",
        "l" to "д",
        ";" to "ж",
        "'" to "э",
        "z" to "я",
        "x" to "ч",
        "c" to "с",
        "v" to "м",
        "b" to "и",
        "n" to "т",
        "m" to "ь",
        "," to "б",
        "." to "ю",
        "/" to ".",
    )
}