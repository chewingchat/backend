package org.chewing.v1.mongorepository

import org.chewing.v1.mongoentity.ChatSequenceMongoEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * 채팅 page를 빠르게 계산하기 위한 Entity
 * 친구 마지막 시퀀스는 나른 곳
 * */
@Repository
internal interface ChatSequenceMongoRepository : MongoRepository<ChatSequenceMongoEntity, String>
