package osu.salat23.circler.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import osu.salat23.circler.persistence.entity.Chat
import java.util.*

@Repository
interface ChatRepository {

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
            insert into chats(client_id, client_type) values (:clientId, :clientType)
        """)
    fun createChat(
        @Param("clientId") clientId: String,
        @Param("clientType") clientTypeName: String
    ): Int
}