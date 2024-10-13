package org.chewing.v1.implementation.chat.room

import org.chewing.v1.model.chat.room.ChatNumber
import org.chewing.v1.repository.ChatRoomMemberRepository
import org.springframework.stereotype.Component

@Component
class ChatRoomUpdater(
    private val chatRoomMemberRepository: ChatRoomMemberRepository
) {
    fun updateUnDelete(chatRoomId: String, userId: String) {
        chatRoomMemberRepository.updateUnDelete(chatRoomId, userId)
    }

    fun updateRead(userId: String, number: ChatNumber) {
        chatRoomMemberRepository.updateRead(userId, number)
    }
}