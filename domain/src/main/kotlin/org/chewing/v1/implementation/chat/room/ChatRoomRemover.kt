package org.chewing.v1.implementation.chat.room

import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.repository.ChatRoomMemberRepository
import org.springframework.stereotype.Component

@Component
class ChatRoomRemover(
    private val chatRoomMemberRepository: ChatRoomMemberRepository
) {
    fun removeMembers(chatRoomIds: List<String>, userId: String){
        return chatRoomMemberRepository.removeChatRoomMembers(chatRoomIds, userId)
    }
}