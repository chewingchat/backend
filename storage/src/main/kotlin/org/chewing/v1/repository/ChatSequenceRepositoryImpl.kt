package org.chewing.v1.repository

import org.chewing.v1.model.chat.room.ChatRoomSequenceNumber
import org.chewing.v1.mongoentity.ChatSequenceMongoEntity
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
internal class ChatSequenceRepositoryImpl(
    private val mongoTemplate: MongoTemplate,
) : ChatSequenceRepository {

    override fun readCurrent(roomId: String): ChatRoomSequenceNumber {
        val sequenceEntity = mongoTemplate.findById(roomId, ChatSequenceMongoEntity::class.java)
        return sequenceEntity?.seqNumber?.let { ChatRoomSequenceNumber.of(it, roomId) }
            ?: ChatRoomSequenceNumber.of(0, roomId)
    }

    override fun updateSequenceIncrement(roomId: String): ChatRoomSequenceNumber {
        val sequenceEntity = mongoTemplate.findById(roomId, ChatSequenceMongoEntity::class.java)
        return if (sequenceEntity != null) {
            sequenceEntity.incrementSeqNumber()
            mongoTemplate.save(sequenceEntity)
            ChatRoomSequenceNumber.of(sequenceEntity.seqNumber, roomId)
        } else {
            val newEntity = ChatSequenceMongoEntity(roomId = roomId, seqNumber = 1)
            mongoTemplate.save(newEntity)
            ChatRoomSequenceNumber.of(1, roomId)
        }
    }

    override fun readSeqNumbers(roomIds: List<String>): List<ChatRoomSequenceNumber> {
        val query = Query(Criteria.where("roomId").`in`(roomIds))

        val entities = mongoTemplate.find(query, ChatSequenceMongoEntity::class.java)

        return entities.map { ChatRoomSequenceNumber.of(it.seqNumber, it.roomId) }
    }
}