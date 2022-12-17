package osu.salat23.circler.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import osu.salat23.circler.bot.commands.Command
import osu.salat23.circler.persistence.entity.Chat
import java.util.*

@Repository
interface ChatRepository : JpaRepository<Chat, Long> {
    fun findChatByClientSpecificId(clientSpecificId: String): Optional<Chat>
}