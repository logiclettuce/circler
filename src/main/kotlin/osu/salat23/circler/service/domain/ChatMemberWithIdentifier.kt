package osu.salat23.circler.service.domain

import osu.salat23.circler.persistence.entity.ChatMember

data class ChatMemberWithIdentifier(
    val chatMember: ChatMember,
    val identifier: String
)