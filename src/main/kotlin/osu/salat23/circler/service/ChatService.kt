package osu.salat23.circler.service

import org.json.JSONObject
import org.springframework.stereotype.Service
import osu.salat23.circler.bot.ClientType
import osu.salat23.circler.api.osu.Server
import osu.salat23.circler.bot.response.templates.Template
import osu.salat23.circler.bot.response.templates.TemplateFactory
import osu.salat23.circler.bot.response.templates.TemplateFormat
import osu.salat23.circler.bot.response.templates.TemplateType
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
    private val chatMemberService: ChatMemberService,
    private val templateFactory: TemplateFactory
) {
    fun setChatTemplate(chat: Chat, type: TemplateType, format: TemplateFormat, template: String) {
        chatRepository.changeChatTemplate(chat.id, type, format, template)
    }

    fun setChatTemplate(chat: Chat, type: TemplateType, format: TemplateFormat, templateFile: InputStream) {
        val  template = BufferedReader(InputStreamReader(templateFile)).readText()
        setChatTemplate(chat, type, format, template)
    }

    fun getChatTemplateAndApplyContext(chat: Chat, type: TemplateType, format: TemplateFormat, context: JSONObject): Template {
        val res = chatRepository.getChatTemplate(chat.id, type, format)
        return templateFactory.applyTemplateContext(type, format, res, context)
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