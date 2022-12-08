package osu.salat23.circler.bot.telegram

object TelegramTextParsingTools {
    fun removePing(text: String, botName: String): String {
        return text.replace("@$botName", "").trim()
    }
}