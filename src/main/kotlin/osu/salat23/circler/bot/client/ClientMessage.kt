package osu.salat23.circler.bot.client

open class ClientMessage(
    chatId: String,
    userId: String,
    val text: String
) : ClientEntity(
    chatId = chatId,
    userId = userId
)