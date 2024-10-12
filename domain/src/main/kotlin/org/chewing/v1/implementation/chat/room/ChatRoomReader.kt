package org.chewing.v1.implementation.chat.room

import org.chewing.v1.model.chat.ChatRoomMemberInfo
import org.chewing.v1.model.chat.room.ChatRoomInfo
import org.chewing.v1.repository.ChatRoomMemberRepository
import org.chewing.v1.repository.ChatRoomRepository
import org.springframework.stereotype.Component

@Component
class ChatRoomReader(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomMemberRepository: ChatRoomMemberRepository
) {
    fun readChatRooms(roomIds: List<String>): List<ChatRoomInfo> {
        return chatRoomRepository.readChatRooms(roomIds)
    }

    fun readChatRoom(roomId: String): ChatRoomInfo {
        return chatRoomRepository.readChatRoom(roomId)
    }

    fun readPersonalChatRoomId(userId: String, friendId: String): String?{
        return chatRoomMemberRepository.readPersonalChatRoomId(userId, friendId)
    }


    fun readUserInChatRooms(roomId: String): List<ChatRoomMemberInfo> {
        return chatRoomMemberRepository.readChatRoomUsers(roomId)
    }

    fun readChatRoomsMember(roomIds: List<String>, userId: String): List<ChatRoomMemberInfo> {
        return chatRoomMemberRepository.readChatRoomsMember(roomIds, userId)
    }
}