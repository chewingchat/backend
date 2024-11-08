package org.chewing.v1.mongorepository

import org.chewing.v1.model.chat.log.ChatLogType
import org.chewing.v1.mongoentity.ChatMessageMongoEntity
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.Update

internal interface ChatLogMongoRepository : MongoRepository<ChatMessageMongoEntity, String> {
    @Query("{ 'chatRoomId': ?0, 'page': ?1 }")
    fun findByRoomIdAndPageSortedBySeqNumber(chatRoomId: String, page: Int, sort: Sort = Sort.by(Sort.Direction.ASC, "seqNumber")): List<ChatMessageMongoEntity>

    @Query("{ \$or: ?0 }")
    fun findByRoomIdAndSeqNumbers(conditions: List<Map<String, Any>>): List<ChatMessageMongoEntity>

    @Modifying
    @Query("{ '_id': ?0 }")
    @Update("{ '\$set': { 'type': ?1 } }")
    fun updateMessageTypeToDelete(messageId: String, deleteType: ChatLogType = ChatLogType.DELETE): Int
}
