package org.chewing.v1

import org.chewing.v1.entity.ChatMessageMongoEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatMessageMongoRepository : MongoRepository<ChatMessageMongoEntity, String> {
    fun findByRoomIdAndPage(roomId: String, page: Int): List<ChatMessageMongoEntity>
}