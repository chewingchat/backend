package org.chewing.v1.facade

import org.chewing.v1.model.media.FileData
import org.chewing.v1.service.chat.ChatLogService
import org.chewing.v1.service.chat.RoomService
import org.chewing.v1.service.notification.NotificationService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatFacade(
    private val chatLogService: ChatLogService,
    private val notificationService: NotificationService
) {
    fun processFiles(fileDataList: List<FileData>, userId: String, chatRoomId: String) {
        val chatMessage = chatLogService.uploadFiles(fileDataList, userId, chatRoomId)
        notificationService.handleMessagesNotification(chatRoomId, userId, chatMessage)
    }

    fun processRead(chatRoomId: String, userId: String) {
        val chatMessage = chatLogService.readMessage(chatRoomId, userId)
        notificationService.handleMessagesNotification(chatRoomId, userId, chatMessage)
    }

    fun processDelete(chatRoomId: String, userId: String, messageId: String) {
        val chatMessage = chatLogService.deleteMessage(chatRoomId, userId, messageId)
        notificationService.handleMessagesNotification(chatRoomId, userId, chatMessage)
    }

    fun processReply(chatRoomId: String, userId: String, parentMessageId: String, text: String) {
        val chatMessage = chatLogService.replyMessage(chatRoomId, userId, parentMessageId, text)
        notificationService.handleMessagesNotification(chatRoomId, userId, chatMessage)
    }

    fun processBombing(chatRoomId: String, userId: String, text: String, expiredAt: LocalDateTime) {
        val chatMessage = chatLogService.bombingMessage(chatRoomId, userId, text,expiredAt)
        notificationService.handleMessagesNotification(chatRoomId, userId, chatMessage)
    }

    fun processChat(chatRoomId: String, userId: String, text: String) {
        val chatMessage = chatLogService.chatCommonMessage(chatRoomId, userId, text)
        notificationService.handleMessagesNotification(chatRoomId, userId, chatMessage)
    }
}