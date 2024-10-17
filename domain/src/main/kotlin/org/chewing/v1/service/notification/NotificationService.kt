package org.chewing.v1.service.notification

import org.chewing.v1.implementation.WebSocketProvider
import org.chewing.v1.implementation.chat.message.ChatSender
import org.chewing.v1.implementation.chat.room.ChatRoomReader
import org.chewing.v1.implementation.feed.feed.FeedReader
import org.chewing.v1.implementation.notification.NotificationGenerator
import org.chewing.v1.implementation.notification.NotificationSender
import org.chewing.v1.implementation.user.user.UserReader
import org.chewing.v1.model.chat.message.ChatMessage
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val userReader: UserReader,
    private val feedReader: FeedReader,
    private val notificationGenerator: NotificationGenerator,
    private val notificationSender: NotificationSender,
    private val chatSender: ChatSender,
    private val webSocketProvider: WebSocketProvider,
    private val chatRoomReader: ChatRoomReader
) {
    fun handleCommentNotification(userId: String, feedId: String) {
        val user = userReader.read(userId)
        val feedInfo = feedReader.readInfo(feedId)
        val pushTokens = userReader.readsPushToken(feedInfo.userId)
        val (fcmList, apnsList) = notificationGenerator.generateCommentNotification(user, pushTokens, feedInfo)
        notificationSender.send(fcmList, apnsList)
    }

    fun handleMessagesNotification(chatRoomId: String, userId: String, chatMessage: ChatMessage) {
        val members = chatRoomReader.readChatRoomFriendMember(chatRoomId, userId)
        val (activeUserIds, noActivateUserIds) = members.map { it.memberId }
            .partition { webSocketProvider.readAll(it).isNotEmpty() }
        chatSender.sendChat(chatMessage, activeUserIds)
        noActivateUserIds.forEach {
            val user = userReader.read(it)
            val pushTokens = userReader.readsPushToken(it)
            val (fcmList, apnsList) = notificationGenerator.generateMessageNotification(user, pushTokens, chatMessage)
            notificationSender.send(fcmList, apnsList)
        }
    }
}