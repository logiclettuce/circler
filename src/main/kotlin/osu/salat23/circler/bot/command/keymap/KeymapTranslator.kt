package osu.salat23.circler.bot.command.keymap

object KeymapTranslator {
    fun translate(input: String, keymap: LayoutKeymap): String {
        val keymap = keymap.getKeymap()
        var output = ""
        for (character in input) {
            val mappedKey = keymap[character.toString()]
            output += mappedKey ?: character
        }
        return output
    }

    fun translate(input: String, keymaps: Collection<LayoutKeymap>, includeOriginal: Boolean = false): List<String> {
        val resultList = mutableSetOf<String>()
        if (includeOriginal) resultList += input
        keymaps.forEach { keymap ->
            resultList += translate(input, keymap)
        }
        return resultList.toList()
    }

    fun translate(inputs: Collection<String>, keymaps: Collection<LayoutKeymap>, includeOriginal: Boolean = false): List<String> {
        val allOutputs = mutableSetOf<String>()
        inputs.forEach { input ->
            allOutputs += translate(input, keymaps, includeOriginal)
        }
        return allOutputs.toList()
    }
}