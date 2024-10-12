package org.chewing.v1.implementation.chat.log


import org.chewing.v1.model.chat.log.ChatLog1
import org.chewing.v1.model.chat.room.ChatRoomNumber
import org.chewing.v1.model.media.Media
import org.chewing.v1.repository.ChatLogRepository
import org.springframework.stereotype.Component

@Component
class ChatLogAppender(
    private val chatLogRepository: ChatLogRepository
) {
    fun appendFileLog(medias: List<Media>, roomId: String, senderId: String, number: ChatRoomNumber): ChatLog1 {
        return chatLogRepository.appendChatFileLog(medias, roomId, senderId, number)
    }
}

