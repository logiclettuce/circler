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


    @Column(name = "html_profile_template", nullable = false)
    var htmlProfileTemplate: String,
    @Column(name = "text_profile_template", nullable = false)
    var textProfileTemplate: String,

    @Column(name = "html_score_template", nullable = false)
    var htmlScoreTemplate: String,
    @Column(name = "text_score_template", nullable = false)
    var textScoreTemplate: String,

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