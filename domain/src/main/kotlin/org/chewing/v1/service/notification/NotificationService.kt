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
        val sessionIds = sessionProvider.readAll(userId)
        chatSender.sendChat(chatMessage, sessionIds)
    }

    fun handleMessagesNotification(userId: String, chatMessage: ChatMessage) {
        // 1. memberId와 session을 함께 가져옴
        val members = chatRoomReader.readChatRoomFriendMember(chatMessage.chatRoomId, userId)
        val (activeSessions, noActiveSessions) = members.map { it.memberId to sessionProvider.readAll(it.memberId) }
            .partition { (_, sessions) -> sessions.isNotEmpty() }

        // 2. activeUserSessions 추출
        val activeUserSessions = activeSessions.flatMap { (_, sessions) -> sessions }

        // 3. 세션이 있는 사용자들에게 메시지 전송
        chatSender.sendChat(chatMessage, activeUserSessions)

        // 4. 세션이 없는 사용자들에게 푸시 알림 전송
        noActiveSessions.forEach { (memberId, _) ->  // 세션 대신 memberId만 사용
            val user = userReader.read(memberId)  // memberId를 사용하여 사용자 정보 조회
            val pushTokens = userReader.readsPushToken(memberId)  // memberId로 푸시 토큰 조회
            val commentNotificationList =
                notificationGenerator.generateMessageNotification(user, pushTokens, chatMessage)
            notificationSender.send(commentNotificationList)
        }
    }

}