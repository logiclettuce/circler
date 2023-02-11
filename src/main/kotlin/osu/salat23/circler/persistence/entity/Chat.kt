package osu.salat23.circler.persistence.entity

import org.hibernate.Hibernate
import osu.salat23.circler.bot.ClientType
import javax.persistence.*

@Entity
@Table(name = "chats")
data class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false)
    var clientType: ClientType,
    @Column(name = "client_id", nullable = false)
    var clientId: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Chat

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , clientType = $clientType , clientSpecificId = $clientId )"
    }

}