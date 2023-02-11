package osu.salat23.circler.bot.client

open class ClientMessage(
    chatId: String,
    userId: String,
    val text: String,
    furtherActions: List<FurtherAction> = emptyList(),
) : ClientEntity(
    chatId = chatId,
    userId = userId,
    furtherActions = furtherActions
)