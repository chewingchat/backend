package org.chewing.v1.facade

import org.chewing.v1.implementation.chat.room.ChatRoomAggregator
import org.chewing.v1.implementation.chat.room.ChatRoomSortEngine
import org.chewing.v1.model.chat.room.ChatRoom
import org.chewing.v1.model.chat.room.ChatRoomSortCriteria
import org.chewing.v1.service.chat.RoomService
import org.chewing.v1.service.chat.ChatService
import org.springframework.stereotype.Service

@Service
class ChatRoomFacade(
    private val chatService: ChatService,
    private val roomService: RoomService,
    private val chatRoomAggregator: ChatRoomAggregator,
) {
    fun leavesChatRoom(chatRoomIds: List<String>, userId: String) {
        roomService.deleteGroupChatRooms(chatRoomIds, userId)
        chatService.processLeaves(chatRoomIds, userId)
    }

    fun createGroupChatRoom(userId: String, friendIds: List<String>): String {
        val newRoomId = roomService.createGroupChatRoom(userId, friendIds)
        chatService.processInvites(friendIds, newRoomId, userId)
        return newRoomId
    }

    fun getChatRooms(userId: String, sort: ChatRoomSortCriteria): List<ChatRoom> {
        val roomInfos = roomService.getChatRooms(userId)
        val chatNumbers = chatService.getLatestChat(roomInfos.map { it.chatRoomId })
        val chatRooms = chatRoomAggregator.aggregateChatRoom(roomInfos, chatNumbers)
        return ChatRoomSortEngine.sortChatRoom(chatRooms, sort)
    }
}