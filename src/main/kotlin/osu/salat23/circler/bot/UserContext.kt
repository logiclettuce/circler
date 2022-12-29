package osu.salat23.circler.bot

data class UserContext (
    val chatId: String,
    val userId: String,
    val clientType: ClientType
)