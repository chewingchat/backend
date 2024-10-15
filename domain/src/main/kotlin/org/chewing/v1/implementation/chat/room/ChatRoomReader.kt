package org.chewing.v1.implementation.chat.room

import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.model.chat.room.ChatRoomInfo
import org.chewing.v1.repository.chat.ChatRoomMemberRepository
import org.chewing.v1.repository.chat.ChatRoomRepository
import org.springframework.stereotype.Component

@Component
class ChatRoomReader(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomMemberRepository: ChatRoomMemberRepository
) {
    fun reads(chatRoomIds: List<String>): List<ChatRoomInfo> {
        return chatRoomRepository.readChatRooms(chatRoomIds)
    }

    fun readPersonalChatRoomId(userId: String, friendId: String): String? {
        return chatRoomMemberRepository.readPersonalChatRoomId(userId, friendId)
    }

    fun readChatRoomFriendMember(chatRoomId: String, userId: String): List<ChatRoomMemberInfo> {
        return chatRoomMemberRepository.readChatRoomFriendMember(chatRoomId, userId)
    }

    fun readsMemberByUserId(userId: String): List<ChatRoomMemberInfo> {
        return chatRoomMemberRepository.readMembersByUserId(userId)
    }
    fun readChatRoomMembersByUserId(userId: String): List<ChatRoomMemberInfo> {
        return chatRoomMemberRepository.readMembersByUserId(userId)
    }
}