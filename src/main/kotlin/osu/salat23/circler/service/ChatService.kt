package osu.salat23.circler.service

import org.springframework.stereotype.Service
import osu.salat23.circler.bot.ClientType
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.response.templates.ResponseTemplates
import osu.salat23.circler.persistence.entity.Chat
import osu.salat23.circler.persistence.repository.ChatRepository
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import javax.transaction.Transactional

@Service
@Transactional
class ChatService(
    private val chatRepository: ChatRepository,
    private val chatMemberService: ChatMemberService
) {
    fun setChatTemplate(templateType: ResponseTemplates, clientId: String, clientType: ClientType, template: String, isHtml: Boolean) {
        val chat = getOrCreateChat(clientId, clientType)

        // todo there is probably a better way of doing this but I cant think of one right now
        when (templateType) {
            ResponseTemplates.Profile -> {
                if (isHtml) chat.htmlProfileTemplate = template
                else chat.textProfileTemplate = template
                chatRepository.save(chat)
            }

            else -> {}
        }

    }

    fun setChatTemplate(templateType: ResponseTemplates, clientId: String, clientType: ClientType, templateFile: InputStream, isHtml: Boolean) {
        val  template = BufferedReader(InputStreamReader(templateFile)).readText()
        setChatTemplate(templateType, clientId, clientType, template, isHtml)
    }

    fun getChatMemberIdentifiers(clientId: String, clientType: ClientType, server: Server): List<String> {
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