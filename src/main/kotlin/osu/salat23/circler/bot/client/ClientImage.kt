package osu.salat23.circler.bot.client

import java.io.InputStream

class ClientImage(
    chatId: String,
    userId: String,
    text: String? = null,
    val image: InputStream,
    furtherActions: List<FurtherAction> = emptyList(),
) : ClientMessage(
    chatId = chatId,
    userId = userId,
    text = text ?: "",
    furtherActions = furtherActions
)