package osu.salat23.circler.service

import org.springframework.stereotype.Service
import osu.salat23.circler.bot.command.commands.Command
import osu.salat23.circler.osu.Server
import osu.salat23.circler.persistence.entity.ChatMember
import osu.salat23.circler.persistence.repository.ChatMemberRepository
import javax.transaction.Transactional

@Service
@Transactional
class ChatMemberService (
    private val chatMemberRepository: ChatMemberRepository
) {

    fun getChatMemberIdentifiers(chatId: Long, server: Server): List<String> {
        return chatMemberRepository.getChatMemberIdentifiers(chatId, server.name)
    }
    fun getOrCreateChatMember(clientId: String, chatId: Long): ChatMember {
        val optionalUser = chatMemberRepository.getChatMember(clientId, chatId)
        return if (optionalUser.isEmpty) {
            chatMemberRepository.createChatMember(clientId, chatId)
            chatMemberRepository
                .getChatMember(clientId, chatId).orElseThrow{ IllegalStateException("Could not create chat member") } // todo create good handling of errors
        } else optionalUser.get()
    }

}