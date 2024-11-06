package org.chewing.v1.jparepository.chat

import org.chewing.v1.jpaentity.chat.ChatRoomJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
internal interface ChatRoomJpaRepository : JpaRepository<ChatRoomJpaEntity, String> {
    fun findByChatRoomId(chatRoomId: String): Optional<ChatRoomJpaEntity>
    fun findByChatRoomIdIn(chatRoomIds: List<String>): List<ChatRoomJpaEntity>
}
