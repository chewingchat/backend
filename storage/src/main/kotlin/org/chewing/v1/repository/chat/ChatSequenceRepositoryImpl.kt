package org.chewing.v1.repository.chat

import org.chewing.v1.model.chat.room.ChatSequenceNumber
import org.chewing.v1.mongoentity.ChatSequenceMongoEntity
import org.chewing.v1.repository.chat.ChatSequenceRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
internal class ChatSequenceRepositoryImpl(
    private val mongoTemplate: MongoTemplate,
) : ChatSequenceRepository {

    override fun readCurrent(chatRoomId: String): ChatSequenceNumber {
        val sequenceEntity = mongoTemplate.findById(chatRoomId, ChatSequenceMongoEntity::class.java)
        return sequenceEntity?.seqNumber?.let { ChatSequenceNumber.of(it, chatRoomId) }
            ?: ChatSequenceNumber.of(0, chatRoomId)
    }

    override fun updateSequenceIncrement(chatRoomId: String): ChatSequenceNumber {
        val sequenceEntity = mongoTemplate.findById(chatRoomId, ChatSequenceMongoEntity::class.java)
        return if (sequenceEntity != null) {
            sequenceEntity.incrementSeqNumber()
            mongoTemplate.save(sequenceEntity)
            ChatSequenceNumber.of(sequenceEntity.seqNumber, chatRoomId)
        } else {
            val newEntity = ChatSequenceMongoEntity(chatRoomId = chatRoomId, seqNumber = 1)
            mongoTemplate.save(newEntity)
            ChatSequenceNumber.of(1, chatRoomId)
        }
    }

    override fun updateSequenceIncrements(chatRoomIds: List<String>): List<ChatSequenceNumber> {
        val query = Query(Criteria.where("chatRoomId").`in`(chatRoomIds))

        return mongoTemplate.find(query, ChatSequenceMongoEntity::class.java)
            .map {
                it.incrementSeqNumber()
                mongoTemplate.save(it)
                ChatSequenceNumber.of(it.seqNumber, it.chatRoomId) // 결과 리스트로 변환
            }
    }

    override fun readSeqNumbers(chatRoomIds: List<String>): List<ChatSequenceNumber> {
        val query = Query(Criteria.where("chatRoomId").`in`(chatRoomIds))
        val entities = mongoTemplate.find(query, ChatSequenceMongoEntity::class.java)
        return entities.map { ChatSequenceNumber.of(it.seqNumber, it.chatRoomId) }
    }
}