package org.chewing.v1.implementation.chat.room

import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.model.chat.room.ChatRoomInfo
import org.chewing.v1.repository.ChatRoomMemberRepository
import org.chewing.v1.repository.ChatRoomRepository
import org.springframework.stereotype.Component

@Component
class ChatRoomReader(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomMemberRepository: ChatRoomMemberRepository
) {
    fun readChatRooms(chatRoomIds: List<String>): List<ChatRoomInfo> {
        return chatRoomRepository.readChatRooms(chatRoomIds)
    }

    fun readPersonalChatRoomId(userId: String, friendId: String): String?{
        return chatRoomMemberRepository.readPersonalChatRoomId(userId, friendId)
    }


    fun readUserInChatRooms(chatRoomId: String): List<ChatRoomMemberInfo> {
        return chatRoomMemberRepository.readChatRoomUsers(chatRoomId)
    }

    fun readChatRoomsMember(chatRoomIds: List<String>, userId: String): List<ChatRoomMemberInfo> {
        return chatRoomMemberRepository.readChatRoomsMember(chatRoomIds, userId)
    }
}