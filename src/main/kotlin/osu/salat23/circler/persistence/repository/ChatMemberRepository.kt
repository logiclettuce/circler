package osu.salat23.circler.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.telegram.telegrambots.meta.api.objects.adminrights.ChatAdministratorRights
import osu.salat23.circler.persistence.entity.ChatMember
import osu.salat23.circler.service.domain.ChatMemberWithIdentifier
import java.util.Optional

@Repository
interface ChatMemberRepository : JpaRepository<ChatMember, Long> {

    @Query(
        nativeQuery = true,
        value =
        """
            select * from chat_members cm where cm.client_id = :clientId and chat_id = :chatId
        """
    )
    fun getChatMember(
        @Param("clientId") clientId: String,
        @Param("chatId") chatId: Long
    ): Optional<ChatMember>

    @Query(
        nativeQuery = true,
        value =
        """
            select cmsi.player_identifier as identifier from chat_members cm join chat_member_server_identifiers cmsi on cm.id = cmsi.chat_member_id 
            where
                cm.chat_id = :chatId and
                cmsi.server = :server
        """
    )
    fun getChatMemberIdentifiers(@Param("chatId") chatId: Long, @Param("server") serverName: String): List<String>

    @Modifying
    @Query(
        nativeQuery = true,
        value =
        """
            insert into chat_members(client_id, chat_id) values (:clientId, :chatId)
        """
    )
    fun createChatMember(
        @Param("clientId") clientId: String,
        @Param("chatId") chatId: Long
    ): Int
}