package org.chewing.v1.mongorepository

import org.chewing.v1.mongoentity.ChatMessageMongoEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatMessageMongoRepository : MongoRepository<ChatMessageMongoEntity, String> {
    fun findByRoomIdAndPage(roomId: String, page: Int): List<ChatMessageMongoEntity>
}