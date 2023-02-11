package osu.salat23.circler.persistence.repository

import org.jooq.DSLContext
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import osu.salat23.circler.Tables.*
import osu.salat23.circler.bot.response.templates.Template
import osu.salat23.circler.bot.response.templates.TemplateType
import osu.salat23.circler.bot.response.templates.TemplateFormat
import osu.salat23.circler.persistence.entity.Chat
import java.util.*

@Repository
class ChatRepository(
    val context: DSLContext
) {
    fun getChat(
        clientId: String,
        clientTypeName: String
    ): Optional<Chat> {
        val res = context
            .select().from(CHATS)

            .where(CHATS.CLIENT_ID.eq(clientId))
            .and(CHATS.CLIENT_TYPE.eq(clientTypeName))

            .fetchOne()?.into(Chat::class.java)
        return Optional.ofNullable(res)
    }

    @Modifying
    fun createChat(
        clientId: String,
        clientTypeName: String
    ): Int {
        return context
            .insertInto(
                CHATS,
                CHATS.CLIENT_ID, CHATS.CLIENT_TYPE
            )
            .values(clientId, clientTypeName)
            .execute()
    }

    @Modifying
    fun changeChatTemplate(
        chatId: Long,
        type: TemplateType,
        format: TemplateFormat,
        template: String
    ): Int {
        val foundTemplateRecordOptional = context
            .select(CHAT_TEMPLATES.ID).from(CHAT_TEMPLATES)
            .where(CHAT_TEMPLATES.CHAT_ID.eq(chatId))
            .and(CHAT_TEMPLATES.TYPE.eq(type.name))
            .and(CHAT_TEMPLATES.FORMAT.eq(format.name))
            .fetchOptional()
        if (foundTemplateRecordOptional.isEmpty) {
            return context
                .insertInto(
                    CHAT_TEMPLATES,
                    CHAT_TEMPLATES.CHAT_ID,
                    CHAT_TEMPLATES.TYPE,
                    CHAT_TEMPLATES.FORMAT,
                    CHAT_TEMPLATES.TEMPLATE
                )
                .values(
                    chatId,
                    type.name,
                    format.name,
                    template
                )
                .execute()
        }
        val chatTemplateId = foundTemplateRecordOptional.get().value1()
        return context
            .update(CHAT_TEMPLATES)
            .set(CHAT_TEMPLATES.TEMPLATE, template)
            .where(CHAT_TEMPLATES.ID.eq(chatTemplateId))
            .execute()
    }

    fun getChatTemplate(
        chatId: Long,
        type: TemplateType,
        format: TemplateFormat,
    ): Template {
        val res = context
            .select(CHAT_TEMPLATES.TEMPLATE).from(CHAT_TEMPLATES)
            .where(CHAT_TEMPLATES.CHAT_ID.eq(chatId))
            .and(CHAT_TEMPLATES.TYPE.eq(type.name))
            .and(CHAT_TEMPLATES.FORMAT.eq(format.name))
            .fetchOne()?.into(String::class.java) ?: ""
        return Template(res, type, format)
    }
}