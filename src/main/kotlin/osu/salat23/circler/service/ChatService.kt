package osu.salat23.circler.service

import org.springframework.stereotype.Service
import osu.salat23.circler.bot.ClientType
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.persistence.repository.ChatMemberRepository
import osu.salat23.circler.persistence.repository.ServerIdentifierRepository

@Service
class ChatService(
    private val serverIdentifierRepository: ServerIdentifierRepository,
    private val chatMemberRepository: ChatMemberRepository,
) {
    fun getPlayerIdentifier(userId: String, chatId: String, server: Command.Server, clientType: ClientType): String? {
        // todo change to correct method
        println("$userId, $chatId, $server")
        return serverIdentifierRepository.getPlayerIdentifier(userId, chatId, server)
    }

    fun setPlayerIdentifier(
        identifier: String,
        userId: String,
        chatId: String,
        server: Command.Server,
        clientType: ClientType
    ) {
        println("Userid: " + userId)
        println("chatid: " + chatId)
        serverIdentifierRepository.setPlayerIdentifier(
            identifier = identifier,
            userId = userId,
            chatId = chatId,
            server = server,
            clientType = clientType
        )
    }
}