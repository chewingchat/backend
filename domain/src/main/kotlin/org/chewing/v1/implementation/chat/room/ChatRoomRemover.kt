package org.chewing.v1.implementation.chat.room

import org.chewing.v1.model.chat.ChatRoomMemberInfo
import org.chewing.v1.repository.ChatRoomMemberRepository
import org.springframework.stereotype.Component

@Component
class ChatRoomRemover(
    private val chatRoomMemberRepository: ChatRoomMemberRepository
) {
    fun removeChatRoomsMember(chatRoomIds: List<String>, userId: String):List<ChatRoomMemberInfo> {
        return chatRoomMemberRepository.removeChatRoomMembers(chatRoomIds, userId)
    }
}