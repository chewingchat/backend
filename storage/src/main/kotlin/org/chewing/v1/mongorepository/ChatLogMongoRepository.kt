package org.chewing.v1.mongorepository

import org.chewing.v1.mongoentity.ChatMessageMongoEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
internal interface ChatLogMongoRepository : MongoRepository<ChatMessageMongoEntity, String> {
    @Query("{ 'chatRoomId': ?0, 'page': ?1 }")
    fun findByRoomIdAndPage(chatRoomId: String, page: Int): List<ChatMessageMongoEntity>
}


