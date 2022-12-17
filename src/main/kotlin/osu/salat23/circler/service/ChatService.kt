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
    fun getChatMemberServerPlayerIdentifier(userId: String, chatId: String, server: Command.Server, clientType: ClientType): String? {
        val chatId = "${clientType.name}_$chatId"
        val userId = "${clientType.name}_$userId"
        // todo change to correct method
        return serverIdentifierRepository.getPlayerIdentifier(userId, chatId, server)
    }

    fun setChatMemberServerPlayerIdentifier(userId: String, chatId: String, server: Command.Server, clientType: ClientType) {
        serverIdentifierRepository.setPlayerIdentifier()
    }
}