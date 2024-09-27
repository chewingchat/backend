package org.chewing.v1.repository

import org.chewing.v1.ChatMessageRedisRepository
import org.chewing.v1.entity.ChatMessageLogRedisEntity
import org.chewing.v1.model.ChatMessageLog
import org.springframework.stereotype.Repository

@Repository
class ChatMessageCacheRepositoryImpl(
    private val chatMessageRedisRepository: ChatMessageRedisRepository
) : ChatMessageCacheRepository {

    override fun appendChatMessageLog(chatMessageLog: ChatMessageLog) {
        chatMessageRedisRepository.save(ChatMessageLogRedisEntity.toEntity(chatMessageLog))
    }

    override fun readChatMessageLog(roomId: String, page: Int): ChatMessageLog? {
        return chatMessageRedisRepository.findChatMessageLogRedisEntityByRoomIdAndPage(roomId, page)?.toChatMessageLog()
    }
}