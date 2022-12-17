package osu.salat23.circler.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.persistence.entity.ServerIdentifier

@Repository
interface ServerIdentifierRepository : JpaRepository<ServerIdentifier,  Long> {

    @Query(
        nativeQuery = true, value = """
        select cmsi.player_identifier from chat_members cm join chats c on c.id = cm.chat_id join chat_member_server_identifiers cmsi on cmsi.chat_member_id=cm.id where cm.client_specific_id = :userId and c.client_specific_id = :chatId and cmsi.server_name = :#{#server.name()}   
        """
    )
    fun getPlayerIdentifier(userId: String, chatId: String, @Param("server") server: Command.Server): String?

    @Query(
        nativeQuery = true, value = """
        select cmsi.id from chat_members cm join chats c on c.id = cm.chat_id join chat_member_server_identifiers cmsi on cmsi.chat_member_id=cm.id where cm.client_specific_id = :userId and c.client_specific_id = :chatId and cmsi.server_name = :#{#server.name()}   
        """
    )
    fun getPlayerIdentifierId(userId: String, chatId: String, @Param("server") server: Command.Server): Long?

    // todo create stored procedure for setting chat member server identifier, preferably create these procedure for above queries as well
    fun setPlayerIdentifier()

    fun  deleteServerIdentifierById(id: Long)

}