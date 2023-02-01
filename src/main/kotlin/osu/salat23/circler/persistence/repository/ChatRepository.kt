package osu.salat23.circler.persistence.repository

import org.jooq.DSLContext
import org.jooq.UpdateSetMoreStep
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import osu.salat23.circler.Tables.*
import osu.salat23.circler.bot.response.templates.ResponseTemplates
import osu.salat23.circler.persistence.entity.Chat
import osu.salat23.circler.tables.records.ChatsRecord
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
        clientId: String,
        clientTypeName: String,
        templateType: ResponseTemplates,
        template: String,
        isHtml: Boolean
    ) {

        val queryBuilderSetStep = context.update(CHATS)
        val queryBuilderWhereStep = when (templateType) {

            ResponseTemplates.Profile -> {
                if (isHtml) queryBuilderSetStep.set(CHATS.HTML_PROFILE_TEMPLATE, template)
                else queryBuilderSetStep.set(CHATS.TEXT_PROFILE_TEMPLATE, template)
            }

        }
        queryBuilderWhereStep
            .where(CHATS.CLIENT_ID.eq(clientId))
            .and(CHATS.CLIENT_TYPE.eq(clientTypeName))
            .execute()
    }
}