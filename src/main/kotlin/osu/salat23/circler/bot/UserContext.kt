package osu.salat23.circler.bot

import java.io.InputStream

data class UserContext (
    val chatId: String,
    val userId: String,
    val clientType: ClientType,
    val isUserAdmin: Boolean,
    val fileAttachment: InputStream
)