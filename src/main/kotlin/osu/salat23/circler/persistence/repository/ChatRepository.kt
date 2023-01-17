package osu.salat23.circler.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import osu.salat23.circler.bot.ClientType
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.persistence.entity.Chat
import java.util.*

@Repository
interface ChatRepository : JpaRepository<Chat, Long> {

    @Query(nativeQuery = true,
        value =
        """
            select * from chats c where c.client_id = :clientId and c.client_type = :clientType    
        """)
    fun getChat(
        @Param("clientId") clientId: String,
        @Param("clientType") clientTypeName: String
    ): Optional<Chat>

    @Modifying
    @Query(nativeQuery = true,
        value =
        """
            update chats c set user_profile_template = :template where c.id = :chatId
        """
    )
    fun setUserProfileTemplate( @Param("chatId") chatId: Long, @Param("template") template: String)

    @Modifying
    @Query(nativeQuery = true,
        value =
        """
            insert into chats(client_id, client_type) values (:clientId, :clientType)
        """)
    fun createChat(
        @Param("clientId") clientId: String,
        @Param("clientType") clientTypeName: String
    ): Int
}