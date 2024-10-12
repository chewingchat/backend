package org.chewing.v1.implementation.chat.room

import org.chewing.v1.repository.ChatRoomMemberRepository
import org.springframework.stereotype.Component

@Component
class ChatRoomUpdater(
    private val chatRoomMemberRepository: ChatRoomMemberRepository
) {
    fun updateUnDelete(roomId: String, userId: String) {
        chatRoomMemberRepository.updateUnDelete(roomId, userId)
    }
}