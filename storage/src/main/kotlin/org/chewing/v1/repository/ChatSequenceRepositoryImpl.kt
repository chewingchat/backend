package org.chewing.v1.repository

import org.chewing.v1.mongoentity.ChatSequenceMongoEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Repository

@Repository
class ChatSequenceRepositoryImpl(private val mongoTemplate: MongoTemplate) : ChatSequenceRepository {

    override fun readCurrentSequence(roomId: String): Long {
        val sequenceEntity = mongoTemplate.findById(roomId, ChatSequenceMongoEntity::class.java)
        return sequenceEntity?.seq ?: 0
    }

    override fun updateSequenceIncrement(roomId: String): Long {
        val sequenceEntity = mongoTemplate.findById(roomId, ChatSequenceMongoEntity::class.java)
        sequenceEntity?.let {
            it.seq += 1
            mongoTemplate.save(it)
            return it.seq
        }
        return 1
    }
}