package osu.salat23.circler.service

import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Service
import osu.salat23.circler.bot.ClientType
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.persistence.entity.Chat
import osu.salat23.circler.persistence.repository.ChatRepository
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors
import javax.transaction.Transactional

@Service
@Transactional
class ChatService(
    private val chatRepository: ChatRepository,
    private val chatMemberService: ChatMemberService
) {

    fun setUserProfileTemplate(clientId: String, clientType: ClientType, template: String) {
        val chat = getOrCreateChat(clientId, clientType)

        chatRepository.setUserProfileTemplate(chat.id, template)
    }

    fun setUserProfileTemplate(clientId: String, clientType: ClientType, templateFile: InputStream) {
        val  template = BufferedReader(InputStreamReader(templateFile)).readText()

        setUserProfileTemplate(clientId, clientType, template)
    }

    fun getChatMemberIdentifiers(clientId: String, clientType: ClientType, server: Command.Server): List<String> {
        val chat = getOrCreateChat(clientId, clientType)

        return chatMemberService.getChatMemberIdentifiers(chat.id, server)
    }
    fun getOrCreateChat(clientId: String, clientType: ClientType): Chat {
        val optionalChat = chatRepository.getChat(clientId, clientType.name)
        return if (optionalChat.isEmpty) {
            chatRepository.createChat(clientId, clientType.name)
            chatRepository.getChat(clientId, clientType.name)
                .orElseThrow { IllegalStateException("Could not create chat") }
        }
        else optionalChat.get()
    }

}