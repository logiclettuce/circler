package osu.salat23.circler.bot.client

class ClientEntity private constructor(
    var chatId: String?,
    val userId: String?,
    val text: String?
) {

    data class Builder(
        private var chatId: String? = null,
        private var text: String? = null,
        private var userId: String? = null,
    ) {
        fun build() = ClientEntity(chatId, userId, text)
        fun chatId(chatId: String) = apply { this.chatId = chatId }
        fun text(text: String) = apply { this.text = text }
        fun userId(userId: String) = apply { this.userId = userId }
    }
}