package osu.salat23.circler.bot.vk

object VkTextParsingTools {
    fun tryRemovePing(messageText: String): String {
        val pingRegex = """\[[\w]*\|@[\w]*\]"""
        return messageText.replace(pingRegex, "").trim()
    }
}