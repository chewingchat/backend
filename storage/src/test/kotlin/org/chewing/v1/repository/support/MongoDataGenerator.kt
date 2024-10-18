package org.chewing.v1.repository.support

import org.chewing.v1.jparepository.auth.EmailJpaRepository
import org.chewing.v1.model.chat.message.ChatMessage
import org.chewing.v1.mongoentity.ChatMessageMongoEntity
import org.chewing.v1.mongorepository.ChatLogMongoRepository
import org.springframework.beans.factory.annotation.Autowired

class MongoDataGenerator {
    @Autowired
    private lateinit var chatLogMongoRepository: ChatLogMongoRepository

    fun chatLogEntityData(chatMessage: ChatMessage) {
        val entity = ChatMessageMongoEntity.fromChatMessage(chatMessage)
        chatLogMongoRepository.save(entity!!)
    }

    fun chatLogEntityDataList(chatMessages: List<ChatMessage>) {
        chatMessages.forEach {
            chatLogEntityData(it)
        }
    }
}