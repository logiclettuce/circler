package osu.salat23.circler.persistence.entity

import org.hibernate.Hibernate
import osu.salat23.circler.bot.commands.Command
import javax.persistence.*

@Entity
@Table(name = "chat_member_server_identifiers")
data class ServerIdentifier(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(name = "player_identifier")
    val playerIdentifier: String,
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "server_name", nullable = false)
    val server: Command.Server,

    @ManyToOne
    val chatMember: ChatMember
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ServerIdentifier

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , playerIdentifier = $playerIdentifier , server = $server )"
    }
}