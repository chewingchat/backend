package org.chewing.v1.repository

import org.chewing.v1.mongoentity.ChatSequenceMongoEntity
import org.chewing.v1.mongorepository.ChatMessageMongoRepository
import org.chewing.v1.mongorepository.ChatSequenceMongoRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class ChatSequenceRepositoryImpl(
    private val mongoTemplate: MongoTemplate,
    private val chatSequenceMongoRepository: ChatSequenceMongoRepository
) : ChatSequenceRepository {

    override fun readCurrentSequence(roomId: String): Long {
        val sequenceEntity = mongoTemplate.findById(roomId, ChatSequenceMongoEntity::class.java)
        return sequenceEntity?.seqNumber ?: 0
    }

    override fun updateSequenceIncrement(roomId: String): Long {
        val sequenceEntity = mongoTemplate.findById(roomId, ChatSequenceMongoEntity::class.java)
        sequenceEntity?.let {
            it.seqNumber += 1
            mongoTemplate.save(it)
            return it.seqNumber
        }
        return 1
    }
    // MongoDB에 채팅방 시퀀스 저장
    override fun saveSequence(roomId: String, userId: String, seqNumber: Long) {
        val sequenceEntity = ChatSequenceMongoEntity(roomId = roomId, userId = userId, seqNumber = seqNumber)
        chatSequenceMongoRepository.save(sequenceEntity)
    }

}