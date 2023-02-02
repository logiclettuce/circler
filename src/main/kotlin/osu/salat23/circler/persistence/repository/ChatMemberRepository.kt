package osu.salat23.circler.persistence.repository

import org.jooq.DSLContext
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import osu.salat23.circler.Tables.*
import osu.salat23.circler.persistence.entity.ChatMember
import java.util.*

@Repository
class ChatMemberRepository(
    val context: DSLContext
) {
    fun getChatMember(
        clientId: String,
        chatId: Long
    ): Optional<ChatMember> {
        val res = context
            .select().from(CHAT_MEMBERS)

            .where(CHAT_MEMBERS.CLIENT_ID.eq(clientId))
            .and(CHAT_MEMBERS.CHAT_ID.eq(chatId))

            .fetchOne()?.into(ChatMember::class.java)
        return Optional.ofNullable(res)
    }

    fun getChatMemberIdentifiers(
        chatId: Long,
        serverName: String
    ): List<String> {
        return context
            .select(CHAT_MEMBER_SERVER_IDENTIFIERS.PLAYER_IDENTIFIER)
            .from(CHAT_MEMBERS)

            .join(CHAT_MEMBER_SERVER_IDENTIFIERS)
            .on(CHAT_MEMBERS.ID.eq(CHAT_MEMBER_SERVER_IDENTIFIERS.CHAT_MEMBER_ID))

            .where(CHAT_MEMBERS.CHAT_ID.eq(chatId))
            .and(CHAT_MEMBER_SERVER_IDENTIFIERS.SERVER.eq(serverName))

            .fetch().into(String::class.java)
    }

    @Modifying
    fun createChatMember(
        @Param("clientId") clientId: String,
        @Param("chatId") chatId: Long
    ): Int {
        return context
            .insertInto(CHAT_MEMBERS, CHAT_MEMBERS.CLIENT_ID, CHAT_MEMBERS.CHAT_ID)
            .values(clientId, chatId)
            .execute()
    }
}