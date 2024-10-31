package org.chewing.v1.service.chat

import org.chewing.v1.implementation.chat.room.*
import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.model.chat.room.Room
import org.springframework.stereotype.Service

@Service
class RoomService(
    private val chatRoomReader: ChatRoomReader,
    private val chatRoomRemover: ChatRoomRemover,
    private val chatRoomEnricher: ChatRoomEnricher,
    private val chatRoomAppender: ChatRoomAppender,
    private val chatRoomHandler: ChatRoomHandler,
) {
    fun getChatRooms(userId: String): List<Room> {
        val chatRoomMembers = chatRoomReader.readChatRoomMembersByUserId(userId)
        val chatRoomInfos = chatRoomReader.reads(chatRoomMembers.map { it.chatRoomId })
        return chatRoomEnricher.enrichChatRooms(
            chatRoomMembers,
            chatRoomInfos,
            userId
        )
    }

    fun deleteGroupChatRooms(chatRoomIds: List<String>, userId: String) {
        chatRoomRemover.removeMembers(chatRoomIds, userId)
    }

    fun deleteChatRoom(chatRoomIds: List<String>, userId: String) {
        chatRoomRemover.removeMembers(chatRoomIds, userId)
    }

    fun createChatRoom(userId: String, friendId: String): String {
        val chatRoomId = chatRoomReader.readPersonalChatRoomId(userId, friendId)
        return if (chatRoomId == null) {
            val newRoomId = chatRoomAppender.append(false)
            chatRoomAppender.appendMembers(newRoomId, listOf(userId, friendId))
            newRoomId
        } else {
            chatRoomHandler.lockActivateChatRoomUser(chatRoomId, userId)
            chatRoomId
        }
    }

    fun createGroupChatRoom(userId: String, friendIds: List<String>): String {
        val newRoomId = chatRoomAppender.append(true)
        chatRoomAppender.appendMembers(newRoomId, friendIds)
        return newRoomId
    }

    fun getChatRoomFriends(chatRoomId: String, userId: String): List<ChatRoomMemberInfo> {
        return chatRoomReader.readChatRoomFriendMember(chatRoomId, userId)
    }
}