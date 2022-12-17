package osu.salat23.circler.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import osu.salat23.circler.bot.ClientType
import osu.salat23.circler.persistence.entity.ChatMember

@Repository
interface ChatMemberRepository : JpaRepository<ChatMember, Long> {

    @Query(
        nativeQuery = true, value = """
        call adduser(:clientId, :chatId, #{#client.name()})
    """
    )
    fun addChatMember(clientId: String, chatId: String, client: ClientType)
}