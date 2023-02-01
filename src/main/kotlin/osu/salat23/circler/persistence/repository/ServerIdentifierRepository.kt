package osu.salat23.circler.persistence.repository

import org.jooq.DSLContext
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import osu.salat23.circler.Tables.CHAT_MEMBER_SERVER_IDENTIFIERS
import java.util.*

@Repository
class ServerIdentifierRepository(
    val context: DSLContext
) {
    fun getPlayerIdentifier(
        chatMemberId: Long,
        serverName: String
    ): Optional<String> {
        val res = context
            .select(CHAT_MEMBER_SERVER_IDENTIFIERS.PLAYER_IDENTIFIER)
            .from(CHAT_MEMBER_SERVER_IDENTIFIERS)

            .where(CHAT_MEMBER_SERVER_IDENTIFIERS.SERVER.eq(serverName))
            .and(CHAT_MEMBER_SERVER_IDENTIFIERS.CHAT_MEMBER_ID.eq(chatMemberId))

            .fetchOne()?.into(String::class.java)
        return Optional.ofNullable(res)
    }

    @Modifying
    fun createPlayerIdentifier(
        chatMemberId: Long,
        serverName: String,
        playerIdentifier: String
    ): Int {
        return context
            .insertInto(CHAT_MEMBER_SERVER_IDENTIFIERS,
                CHAT_MEMBER_SERVER_IDENTIFIERS.CHAT_MEMBER_ID,
                CHAT_MEMBER_SERVER_IDENTIFIERS.PLAYER_IDENTIFIER,
                CHAT_MEMBER_SERVER_IDENTIFIERS.SERVER)
            .values(chatMemberId, playerIdentifier, serverName)
            .execute()
    }

    @Modifying
    fun setPlayerIdentifier(
        chatMemberId: Long,
        serverName: String,
        playerIdentifier: String
    ): Int {
        return context
            .update(CHAT_MEMBER_SERVER_IDENTIFIERS)
            .set(CHAT_MEMBER_SERVER_IDENTIFIERS.PLAYER_IDENTIFIER, playerIdentifier)

            .where(CHAT_MEMBER_SERVER_IDENTIFIERS.CHAT_MEMBER_ID.eq(chatMemberId))
            .and(CHAT_MEMBER_SERVER_IDENTIFIERS.SERVER.eq(serverName))

            .execute()
    }
}