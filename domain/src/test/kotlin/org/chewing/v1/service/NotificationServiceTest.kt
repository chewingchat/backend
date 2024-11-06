package org.chewing.v1.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.chewing.v1.TestDataFactory
import org.chewing.v1.external.ExternalChatNotificationClient
import org.chewing.v1.external.ExternalPushNotificationClient
import org.chewing.v1.external.ExternalSessionClient
import org.chewing.v1.implementation.notification.NotificationGenerator
import org.chewing.v1.implementation.notification.NotificationSender
import org.chewing.v1.implementation.session.SessionProvider
import org.chewing.v1.implementation.user.user.UserReader
import org.chewing.v1.model.notification.Notification
import org.chewing.v1.repository.user.PushNotificationRepository
import org.chewing.v1.repository.user.UserRepository
import org.chewing.v1.service.notification.NotificationService
import org.chewing.v1.util.AsyncJobExecutor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.*

class NotificationServiceTest {
    private val userRepository: UserRepository = mock()
    private val pushNotificationRepository: PushNotificationRepository = mock()
    private val externalPushNotificationClient: ExternalPushNotificationClient = mock()
    private val externalChatNotificationClient: ExternalChatNotificationClient = mock()
    private val externalSessionClient: ExternalSessionClient = mock()

    private val userReader: UserReader = UserReader(userRepository, pushNotificationRepository)
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val asyncJobExecutor = AsyncJobExecutor(ioScope)
    private val notificationSender: NotificationSender =
        NotificationSender(externalPushNotificationClient, externalChatNotificationClient, asyncJobExecutor)
    private val sessionProvider: SessionProvider = SessionProvider(externalSessionClient)
    private val notificationGenerator = NotificationGenerator()
    private val notificationService =
        NotificationService(userReader, notificationGenerator, notificationSender, sessionProvider)

    @Test
    fun `코멘트 앱 푸시 알림 전송 성공`() {
        // given
        val userId = "userId"
        val feedId = "feedId"
        val comment = "comment"
        val user = TestDataFactory.createUser(userId)
        val pushToken = TestDataFactory.createPushToken(userId)
        val captor = argumentCaptor<Notification>()

        whenever(userRepository.read(userId)).thenReturn(user)
        whenever(pushNotificationRepository.reads(userId)).thenReturn(listOf(pushToken, pushToken))
        // when
        notificationService.handleCommentNotification(userId, feedId, comment)
        // then
        verify(externalPushNotificationClient, times(2)).sendFcmNotification(captor.capture())

        val notifications = captor.allValues
        notifications.forEach {
            assertEquals(userId, it.user.userId)
            assertEquals(pushToken, it.pushToken)
            assertEquals(comment, it.content)
            assertEquals(feedId, it.targetId)
        }
    }

    @Test
    fun `채팅 메시지 웹소켓 푸시 전송자에게 알림 전송 성공`() {
        val messageId = "messageId"
        val chatRoomId = "chatRoomId"
        val userId = "userId"
        // given
        val chatMessage = TestDataFactory.createChatNormalMessage(messageId, chatRoomId, userId)
        // when
        assertDoesNotThrow {
            notificationService.handleOwnedMessageNotification(chatMessage)
        }
    }

    @Test
    fun `채팅 메시지 웹소켓 친구에게 알림 전송 성공`() {
        val messageId = "messageId"
        val chatRoomId = "chatRoomId"
        val userId = "userId"
        val targetUserId = "targetId"

        val chatMessage = TestDataFactory.createChatNormalMessage(messageId, chatRoomId, userId)
        whenever(externalSessionClient.isOnline(targetUserId)).thenReturn(true)
        verify(externalChatNotificationClient, never()).sendMessage(chatMessage, targetUserId)
    }

    @Test
    fun `채팅 메시지 웹소켓 알림 전송`() {
        val messageId = "messageId"
        val chatRoomId = "chatRoomId"
        val userId = "userId"
        val targetUserId = "targetId"

        val chatMessage = TestDataFactory.createChatNormalMessage(messageId, chatRoomId, userId)
        whenever(externalSessionClient.isOnline(targetUserId)).thenReturn(false)
        verify(externalChatNotificationClient, never()).sendMessage(chatMessage, targetUserId)
    }

    @Test
    fun `채팅 메시지 푸시 알림 전송 - 일반 메시지`() {
        val userId = "userId"
        val friendId = "friendId"
        val user = TestDataFactory.createUser(userId)
        val pushToken = TestDataFactory.createPushToken(friendId)
        val chatMessage = TestDataFactory.createChatNormalMessage("messageId", "chatRoomId", userId)
        val captor = argumentCaptor<Notification>()

        whenever(userRepository.read(userId)).thenReturn(user)
        whenever(pushNotificationRepository.reads(friendId)).thenReturn(listOf(pushToken))
        whenever(externalSessionClient.isOnline(friendId)).thenReturn(false)
        // when
        notificationService.handleMessagesNotification(chatMessage, listOf(friendId), userId)
        // then
        verify(externalPushNotificationClient).sendFcmNotification(captor.capture())

        val notification = captor.firstValue
        assertEquals(userId, notification.user.userId)
        assertEquals(pushToken, notification.pushToken)
        assertEquals(chatMessage.text, notification.content)
        assertEquals(chatMessage.chatRoomId, notification.targetId)
    }

    @Test
    fun `채팅 메시지 푸시 알림 전송 - 폭탄 메시지`() {
        val userId = "userId"
        val friendId = "friendId"
        val user = TestDataFactory.createUser(userId)
        val pushToken = TestDataFactory.createPushToken(friendId)
        val chatMessage = TestDataFactory.createBombMessage("messageId", "chatRoomId")
        val captor = argumentCaptor<Notification>()

        whenever(userRepository.read(userId)).thenReturn(user)
        whenever(pushNotificationRepository.reads(friendId)).thenReturn(listOf(pushToken))
        whenever(externalSessionClient.isOnline(friendId)).thenReturn(false)
        // when
        notificationService.handleMessagesNotification(chatMessage, listOf(friendId), userId)
        // then
        verify(externalPushNotificationClient).sendFcmNotification(captor.capture())

        val notification = captor.firstValue
        assertEquals(userId, notification.user.userId)
        assertEquals(pushToken, notification.pushToken)
        assertEquals(chatMessage.text, notification.content)
        assertEquals(chatMessage.chatRoomId, notification.targetId)
    }

    @Test
    fun `채팅 메시지 푸시 알림 전송되지 않음 - 읽음 메시지`() {
        val userId = "userId"
        val friendId = "friendId"
        val user = TestDataFactory.createUser(userId)
        val pushToken = TestDataFactory.createPushToken(friendId)
        val chatMessage = TestDataFactory.createReadMessage("chatRoomId")

        whenever(userRepository.read(userId)).thenReturn(user)
        whenever(pushNotificationRepository.reads(friendId)).thenReturn(listOf(pushToken))
        whenever(externalSessionClient.isOnline(friendId)).thenReturn(false)
        // when
        notificationService.handleMessagesNotification(chatMessage, listOf(friendId), userId)
        // then
        verify(externalPushNotificationClient, never()).sendFcmNotification(any())
    }

    @Test
    fun `채팅 메시지 푸시 알림 전송 - 댓긓 메시지`() {
        val userId = "userId"
        val friendId = "friendId"
        val user = TestDataFactory.createUser(userId)
        val pushToken = TestDataFactory.createPushToken(friendId)
        val chatMessage = TestDataFactory.createReplyMessage("messageId", "chatRoomId")
        val captor = argumentCaptor<Notification>()

        whenever(userRepository.read(userId)).thenReturn(user)
        whenever(pushNotificationRepository.reads(friendId)).thenReturn(listOf(pushToken))
        whenever(externalSessionClient.isOnline(friendId)).thenReturn(false)
        // when
        notificationService.handleMessagesNotification(chatMessage, listOf(friendId), userId)
        // then
        verify(externalPushNotificationClient).sendFcmNotification(captor.capture())

        val notification = captor.firstValue
        assertEquals(userId, notification.user.userId)
        assertEquals(pushToken, notification.pushToken)
        assertEquals(chatMessage.text, notification.content)
        assertEquals(chatMessage.chatRoomId, notification.targetId)
    }

    @Test
    fun `채팅 메시지 푸시 알림 전송 - 파일 메시지`() {
        val userId = "userId"
        val friendId = "friendId"
        val user = TestDataFactory.createUser(userId)
        val pushToken = TestDataFactory.createPushToken(friendId)
        val chatMessage = TestDataFactory.createFileMessage("messageId", "chatRoomId")
        val captor = argumentCaptor<Notification>()

        whenever(userRepository.read(userId)).thenReturn(user)
        whenever(pushNotificationRepository.reads(friendId)).thenReturn(listOf(pushToken))
        whenever(externalSessionClient.isOnline(friendId)).thenReturn(false)
        // when
        notificationService.handleMessagesNotification(chatMessage, listOf(friendId), userId)
        // then
        verify(externalPushNotificationClient).sendFcmNotification(captor.capture())

        val notification = captor.firstValue
        assertEquals(userId, notification.user.userId)
        assertEquals(pushToken, notification.pushToken)
        assertEquals(chatMessage.medias.first().url, notification.content)
        assertEquals(chatMessage.chatRoomId, notification.targetId)
    }

    @Test
    fun `채팅 메시지 푸시 알림 전송 - 초대 메시지`() {
        val userId = "userId"
        val friendId = "friendId"
        val user = TestDataFactory.createUser(userId)
        val pushToken = TestDataFactory.createPushToken(friendId)
        val chatMessage = TestDataFactory.createInviteMessage("messageId", "chatRoomId")
        val captor = argumentCaptor<Notification>()

        whenever(userRepository.read(userId)).thenReturn(user)
        whenever(pushNotificationRepository.reads(friendId)).thenReturn(listOf(pushToken))
        whenever(externalSessionClient.isOnline(friendId)).thenReturn(false)
        // when
        notificationService.handleMessagesNotification(chatMessage, listOf(friendId), userId)
        // then
        verify(externalPushNotificationClient).sendFcmNotification(captor.capture())

        val notification = captor.firstValue
        assertEquals(userId, notification.user.userId)
        assertEquals(pushToken, notification.pushToken)
        assertEquals("", notification.content)
        assertEquals(chatMessage.chatRoomId, notification.targetId)
    }

    @Test
    fun `채팅 메시지 푸시 알림 전송 - 나가기 메시지`() {
        val userId = "userId"
        val friendId = "friendId"
        val user = TestDataFactory.createUser(userId)
        val pushToken = TestDataFactory.createPushToken(friendId)
        val chatMessage = TestDataFactory.createLeaveMessage("messageId", "chatRoomId")
        val captor = argumentCaptor<Notification>()

        whenever(userRepository.read(userId)).thenReturn(user)
        whenever(pushNotificationRepository.reads(friendId)).thenReturn(listOf(pushToken))
        whenever(externalSessionClient.isOnline(friendId)).thenReturn(false)
        // when
        notificationService.handleMessagesNotification(chatMessage, listOf(friendId), userId)
        // then
        verify(externalPushNotificationClient).sendFcmNotification(captor.capture())

        val notification = captor.firstValue
        assertEquals(userId, notification.user.userId)
        assertEquals(pushToken, notification.pushToken)
        assertEquals("", notification.content)
        assertEquals(chatMessage.chatRoomId, notification.targetId)
    }
}
