package org.chewing.v1

import org.chewing.v1.entity.ChatMessageLogRedisEntity
import org.springframework.data.repository.CrudRepository

interface ChatMessageRedisRepository : CrudRepository<ChatMessageLogRedisEntity, String> {
    fun findChatMessageLogRedisEntityByRoomIdAndPage(roomId: String, page: Int): ChatMessageLogRedisEntity?
}