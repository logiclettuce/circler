package osu.salat23.circler.service

import org.springframework.stereotype.Service
import osu.salat23.circler.bot.ClientType
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.osu.Server
import osu.salat23.circler.persistence.repository.ServerIdentifierRepository
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class UserServerIdentifierService(
    private val chatService: ChatService,
    private val chatMemberService: ChatMemberService,
    private val serverIdentifierRepository: ServerIdentifierRepository,
) {
    fun getUserServerIdentifier(
        userClientId: String,
        chatClientId: String,
        server: Server,
        clientType: ClientType
    ): Optional<String> {
        val chat = chatService.getOrCreateChat(chatClientId, clientType)
        val user = chatMemberService.getOrCreateChatMember(userClientId, chat.id)
        return serverIdentifierRepository.getPlayerIdentifier(user.id, server.name)
    }

    fun setUserServerIdentifier(
        identifier: String,
        userClientId: String,
        chatClientId: String,
        server: Server,
        clientType: ClientType
    ): String {
        val chat = chatService.getOrCreateChat(chatClientId, clientType)

        val user = chatMemberService.getOrCreateChatMember(userClientId, chat.id)

        val isIdentifierCreated = serverIdentifierRepository.getPlayerIdentifier(user.id, server.name).isPresent

        return if (isIdentifierCreated) {
            serverIdentifierRepository.setPlayerIdentifier(user.id, server.name, identifier)
            serverIdentifierRepository.getPlayerIdentifier(user.id, server.name).orElseThrow { IllegalStateException("Could not change player identifier")}
        } else {
            serverIdentifierRepository.createPlayerIdentifier(user.id, server.name, identifier)
            serverIdentifierRepository.getPlayerIdentifier(user.id, server.name).orElseThrow { IllegalStateException("Could not create player identifier") }
        }
    }

}