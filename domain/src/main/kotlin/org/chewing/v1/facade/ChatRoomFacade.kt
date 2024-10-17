package org.chewing.v1.facade

import org.chewing.v1.implementation.chat.room.ChatRoomAggregator
import org.chewing.v1.implementation.chat.room.ChatRoomSortEngine
import org.chewing.v1.model.chat.room.ChatRoom
import org.chewing.v1.model.chat.room.ChatRoomSortCriteria
import org.chewing.v1.service.chat.RoomService
import org.chewing.v1.service.chat.ChatLogService
import org.chewing.v1.service.notification.NotificationService
import org.springframework.stereotype.Service

@Service
class ChatRoomFacade(
    private val chatLogService: ChatLogService,
    private val roomService: RoomService,
    private val chatRoomAggregator: ChatRoomAggregator,
    private val notificationService: NotificationService
) {
    fun leavesChatRoom(chatRoomIds: List<String>, userId: String) {
        roomService.deleteGroupChatRooms(chatRoomIds, userId)
        val chatMessages = chatLogService.leaveMessages(chatRoomIds, userId)
        chatMessages.forEach {
            notificationService.handleMessagesNotification(it.chatRoomId, userId, it)
        }
    }

    fun createGroupChatRoom(userId: String, friendIds: List<String>): String {
        val newRoomId = roomService.createGroupChatRoom(userId, friendIds)
        val chatMessages = chatLogService.inviteMessages(friendIds, newRoomId, userId)
        chatMessages.forEach {
            notificationService.handleMessagesNotification(newRoomId, userId, it)
        }
        return newRoomId
    }

    fun getChatRooms(userId: String, sort: ChatRoomSortCriteria): List<ChatRoom> {
        val roomInfos = roomService.getChatRooms(userId)
        val chatNumbers = chatLogService.getLatestChat(roomInfos.map { it.chatRoomId })
        val chatRooms = chatRoomAggregator.aggregateChatRoom(roomInfos, chatNumbers)
        return ChatRoomSortEngine.sortChatRoom(chatRooms, sort)
    }


}