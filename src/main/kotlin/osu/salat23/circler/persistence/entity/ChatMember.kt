package osu.salat23.circler.persistence.entity

import org.hibernate.Hibernate
import osu.salat23.circler.bot.ClientType
import javax.persistence.*

@Entity
@Table(name = "chat_members")
data class ChatMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long,
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "client_type", nullable = false)
    var clientType: ClientType,
    @Column(name = "client_specific_id", nullable = false)
    var clientSpecificId: String,

    @ManyToOne
    var chat: Chat,

    @OneToMany
    var serverIdentifier: List<ServerIdentifier>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ChatMember

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return "ChatMember(id=$id, clientType=$clientType, clientSpecificId='$clientSpecificId', chat=$chat)"
    }


}