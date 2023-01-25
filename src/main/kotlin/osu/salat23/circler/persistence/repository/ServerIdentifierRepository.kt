package osu.salat23.circler.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import osu.salat23.circler.persistence.entity.ServerIdentifier
import java.util.*

@Repository
interface ServerIdentifierRepository : JpaRepository<ServerIdentifier, Long> {

    @Query(nativeQuery = true,
        value =
        """
            select cmsi.player_identifier 
            from chat_member_server_identifiers cmsi
            where 
                cmsi.server = :server and
                cmsi.chat_member_id = :chatMemberId
        """)
    fun getPlayerIdentifier(@Param("chatMemberId") chatMemberId: Long, @Param("server") serverName: String): Optional<String>


    @Modifying
    @Query(nativeQuery = true,
        value =
        """
            insert into chat_member_server_identifiers(chat_member_id, player_identifier, server)
                 values (:chatMemberId, :playerIdentifier, :server)
        """)
    fun createPlayerIdentifier(@Param("chatMemberId") chatMemberId: Long, @Param("server") serverName: String, @Param("playerIdentifier") playerIdentifier: String): Int

    @Modifying
    @Query(nativeQuery = true,
        value =
        """
            update chat_member_server_identifiers cmsi set player_identifier = :playerIdentifier where cmsi.chat_member_id = :chatMemberId and cmsi.server = :server
        """)
    fun setPlayerIdentifier(@Param("chatMemberId") chatMemberId: Long, @Param("server") serverName: String, @Param("playerIdentifier") playerIdentifier: String): Int
}