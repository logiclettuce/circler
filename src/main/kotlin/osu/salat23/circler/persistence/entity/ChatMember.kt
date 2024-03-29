package osu.salat23.circler.persistence.entity

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "chat_members")
data class ChatMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long,
    @Column(name = "client_id", nullable = false)
    var clientId: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ChatMember

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return "ChatMember(id=$id, clientSpecificId='$clientId')"
    }


}