package osu.salat23.circler.bot.command.keymap

/**
 * Define custom language keymap for translating its key presses to english letters
 */
interface LayoutKeymap {
    fun getKeymap(): Map<String, String>
}