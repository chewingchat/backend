package org.chewing.v1.facade

import org.chewing.v1.TestDataFactory
import org.chewing.v1.service.chat.ChatLogService
import org.chewing.v1.service.chat.RoomService
import org.chewing.v1.service.notification.NotificationService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

class ChatFacadeTest {
    private val chatLogService: ChatLogService = mock()
    private val roomService: RoomService = mock()
    private val notificationService: NotificationService = mock()

    private val chatFacade = ChatFacade(chatLogService, roomService, notificationService)

    @Test
    fun `파일 전송 채팅 처리`() {
        val messageId = "messageId"
        val userId = "userId"
        val chatRoomId = "chatRoomId"

        val chatMessage = TestDataFactory.createFileMessage(messageId, chatRoomId)
        val chatRoomMemberInfo = TestDataFactory.createChatRoomMemberInfo(userId, chatRoomId, 1, false)

        whenever(chatLogService.uploadFiles(any(), any(), any())).thenReturn(chatMessage)
        whenever(roomService.getChatRoomFriends(any(), any())).thenReturn(listOf(chatRoomMemberInfo))

        chatFacade.processFiles(listOf(), userId, chatRoomId)

        verify(notificationService).handleMessagesNotification(chatMessage, listOf(chatRoomMemberInfo.memberId), userId)
    }

    @Test
    fun `읽음 처리`() {
        val messageId = "messageId"
        val userId = "userId"
        val chatRoomId = "chatRoomId"

        val chatMessage = TestDataFactory.createReadMessage(messageId)
        val chatRoomMemberInfo = TestDataFactory.createChatRoomMemberInfo(userId, chatRoomId, 1, false)

        whenever(chatLogService.readMessage(any(), any())).thenReturn(chatMessage)
        whenever(roomService.getChatRoomFriends(any(), any())).thenReturn(listOf(chatRoomMemberInfo))

        chatFacade.processRead(chatRoomId, userId)

        verify(notificationService).handleMessagesNotification(chatMessage, listOf(chatRoomMemberInfo.memberId), userId)
    }


    @Test
    fun `삭제 처리`() {
        val messageId = "messageId"
        val userId = "userId"
        val chatRoomId = "chatRoomId"

        val chatMessage = TestDataFactory.createDeleteMessage(messageId, chatRoomId)
        val chatRoomMemberInfo = TestDataFactory.createChatRoomMemberInfo(userId, chatRoomId, 1, false)

        whenever(chatLogService.deleteMessage(any(), any(), any())).thenReturn(chatMessage)
        whenever(roomService.getChatRoomFriends(any(), any())).thenReturn(listOf(chatRoomMemberInfo))

        chatFacade.processDelete(chatRoomId, userId, messageId)

        verify(notificationService).handleMessagesNotification(chatMessage, listOf(chatRoomMemberInfo.memberId), userId)
    }

    @Test
    fun `답장 처리`() {
        val messageId = "messageId"
        val userId = "userId"
        val chatRoomId = "chatRoomId"
        val parentMessageId = "parentMessageId"

        val chatMessage = TestDataFactory.createReplyMessage(messageId, parentMessageId)
        val chatRoomMemberInfo = TestDataFactory.createChatRoomMemberInfo(userId, chatRoomId, 1, false)

        whenever(chatLogService.replyMessage(any(), any(), any(), any())).thenReturn(chatMessage)
        whenever(roomService.getChatRoomFriends(any(), any())).thenReturn(listOf(chatRoomMemberInfo))

        chatFacade.processReply(chatRoomId, userId, parentMessageId, "text")

        verify(notificationService).handleMessagesNotification(chatMessage, listOf(chatRoomMemberInfo.memberId), userId)
    }

    @Test
    fun `폭탄 처리`() {
        val messageId = "messageId"
        val userId = "userId"
        val chatRoomId = "chatRoomId"

        val chatMessage = TestDataFactory.createBombMessage(messageId,chatRoomId)
        val chatRoomMemberInfo = TestDataFactory.createChatRoomMemberInfo(userId, chatRoomId, 1, false)

        whenever(chatLogService.bombingMessage(any(), any(), any(), any())).thenReturn(chatMessage)
        whenever(roomService.getChatRoomFriends(any(), any())).thenReturn(listOf(chatRoomMemberInfo))

        chatFacade.processBombing(chatRoomId, userId, "text", LocalDateTime.now())

        verify(notificationService).handleMessagesNotification(chatMessage, listOf(chatRoomMemberInfo.memberId), userId)
    }

    @Test
    fun `일반 채팅 처리`() {
        val messageId = "messageId"
        val userId = "userId"
        val chatRoomId = "chatRoomId"

        val chatMessage = TestDataFactory.createNormalMessage(messageId, chatRoomId)
        val chatRoomMemberInfo = TestDataFactory.createChatRoomMemberInfo(userId, chatRoomId, 1, false)

        whenever(chatLogService.chatNormalMessage(any(), any(), any())).thenReturn(chatMessage)
        whenever(roomService.getChatRoomFriends(any(), any())).thenReturn(listOf(chatRoomMemberInfo))

        chatFacade.processCommon(chatRoomId, userId, "text")

        verify(notificationService).handleMessagesNotification(chatMessage, listOf(chatRoomMemberInfo.memberId), userId)
    }
}