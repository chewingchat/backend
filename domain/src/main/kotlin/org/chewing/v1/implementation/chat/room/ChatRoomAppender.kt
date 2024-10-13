package org.chewing.v1.implementation.chat.room

import org.chewing.v1.repository.ChatRoomMemberRepository
import org.chewing.v1.repository.ChatRoomRepository
import org.springframework.stereotype.Component

@Component
class ChatRoomAppender(
    private val chatRoomMemberRepository: ChatRoomMemberRepository,
    private val chatRoomRepository: ChatRoomRepository
) {
    fun appendChatRoomMember(chatRoomId: String, userId: String) {
        chatRoomMemberRepository.appendChatRoomMember(chatRoomId, userId)
    }

    fun appendChatRoom(isGroup: Boolean): String {
        return chatRoomRepository.appendChatRoom(isGroup)
    }
    fun appendChatRoomMembers(chatRoomId: String, userIds: List<String>) {
        chatRoomMemberRepository.appendChatRoomMembers(chatRoomId, userIds)
    }
}