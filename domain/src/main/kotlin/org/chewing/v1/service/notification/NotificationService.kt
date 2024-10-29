package org.chewing.v1.service.notification

import org.chewing.v1.implementation.SessionProvider
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
    private val sessionProvider: SessionProvider,
    private val chatRoomReader: ChatRoomReader
) {
    fun handleCommentNotification(userId: String, feedId: String) {
        val user = userReader.read(userId)
        val feedInfo = feedReader.readInfo(feedId)
        val pushTokens = userReader.readsPushToken(feedInfo.userId)
        val commentNotificationList = notificationGenerator.generateCommentNotification(user, pushTokens, feedInfo)
        notificationSender.send(commentNotificationList)
    }

    // sender에게 메시지 알림
    fun handleOwnedMessageNotification(userId: String, chatMessage: ChatMessage) {
        chatSender.sendChat(chatMessage, userId)
    }

    fun handleMessageNotification(chatMessage: ChatMessage) {
        val members = chatRoomReader.readChatRoomFriendMember(chatMessage.chatRoomId, chatMessage.senderId)
        val memberIds = members.map { it.memberId }

        chatSender.sendsChat(chatMessage, memberIds)

        memberIds.forEach { memberId ->
            // 온라인 상태 확인
            if (!sessionProvider.isOnline(memberId)) {
                // 오프라인 유저에게 푸시 알림 전송
                val user = userReader.read(memberId)
                val pushTokens = userReader.readsPushToken(memberId)
                val notificationList = notificationGenerator.generateMessageNotification(user, pushTokens, chatMessage)
                notificationSender.send(notificationList)
            }
        }
    }
}