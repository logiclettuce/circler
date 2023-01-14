package osu.salat23.circler.bot.client

import java.io.InputStream

class ClientImage(
    chatId: String,
    userId: String,
    text: String? = null,
    val image: InputStream
) : ClientMessage(
    chatId = chatId,
    userId = userId,
    text = text ?: ""
)