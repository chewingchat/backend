package org.chewing.v1.repository

import org.chewing.v1.config.MongoContextTest
import org.chewing.v1.model.chat.log.*
import org.chewing.v1.mongorepository.ChatLogMongoRepository
import org.chewing.v1.repository.chat.ChatLogRepositoryImpl
import org.chewing.v1.repository.support.ChatMessageProvider
import org.chewing.v1.repository.support.MongoDataGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.temporal.ChronoUnit

class ChatLogRepositoryTest : MongoContextTest() {
    @Autowired
    private lateinit var chatLogMongoRepository: ChatLogMongoRepository

    @Autowired
    private lateinit var mongoDataGenerator: MongoDataGenerator

    private val chatLogRepositoryImpl: ChatLogRepositoryImpl by lazy {
        ChatLogRepositoryImpl(chatLogMongoRepository)
    }

    @Test
    fun `채팅 로그 읽기 - 존재하지 않음`() {
        val chatLog = chatLogRepositoryImpl.readChatMessage("testRoomId")
        assert(chatLog == null)
    }

    @Test
    fun `일반 채팅 로그 읽기 - 존재함`() {
        val messageId = "testMessageId1"
        val chatRoomId = "testRoomId1"
        val chatMessage = ChatMessageProvider.buildNormalMessage(messageId, chatRoomId)
        mongoDataGenerator.chatLogEntityData(chatMessage)
        val chatLog = chatLogRepositoryImpl.readChatMessage(messageId) as ChatNormalLog
        assert(chatLog.messageId == messageId)
        assert(chatLog.chatRoomId == chatRoomId)
        assert(chatLog.senderId == chatMessage.senderId)
        assert(chatLog.text == chatMessage.text)
        assert(chatLog.number.sequenceNumber == chatMessage.number.sequenceNumber)
        assert(chatLog.number.page == chatMessage.number.page)
        assert(chatLog.timestamp.truncatedTo(ChronoUnit.MILLIS).equals(chatMessage.timestamp.truncatedTo(ChronoUnit.MILLIS)));
        assert(chatLog.type == ChatLogType.NORMAL)
    }

    @Test
    fun `채팅방 나감 채팅 로그 읽기 - 존재함`() {
        val messageId = "testMessageId2"
        val chatRoomId = "testRoomId2"
        val chatMessage = ChatMessageProvider.buildLeaveMessage(messageId, chatRoomId)
        mongoDataGenerator.chatLogEntityData(chatMessage)
        val chatLog = chatLogRepositoryImpl.readChatMessage(messageId) as ChatLeaveLog
        assert(chatLog.messageId == messageId)
        assert(chatLog.chatRoomId == chatRoomId)
        assert(chatLog.senderId == chatMessage.senderId)
        assert(chatLog.number.sequenceNumber == chatMessage.number.sequenceNumber)
        assert(chatLog.number.page == chatMessage.number.page)
        assert(chatLog.timestamp.truncatedTo(ChronoUnit.MILLIS).equals(chatMessage.timestamp.truncatedTo(ChronoUnit.MILLIS)));
        assert(chatLog.type == ChatLogType.LEAVE)
    }

    @Test
    fun `채팅방 초대 채팅 로그 읽기 - 존재함`() {
        val messageId = "testMessageId3"
        val chatRoomId = "testRoomId3"
        val chatMessage = ChatMessageProvider.buildInviteMessage(messageId, chatRoomId)
        mongoDataGenerator.chatLogEntityData(chatMessage)
        val chatLog = chatLogRepositoryImpl.readChatMessage(messageId) as ChatInviteLog
        assert(chatLog.messageId == messageId)
        assert(chatLog.chatRoomId == chatRoomId)
        assert(chatLog.senderId == chatMessage.senderId)
        assert(chatLog.number.sequenceNumber == chatMessage.number.sequenceNumber)
        assert(chatLog.number.page == chatMessage.number.page)
        assert(chatLog.timestamp.truncatedTo(ChronoUnit.MILLIS).equals(chatMessage.timestamp.truncatedTo(ChronoUnit.MILLIS)));
        assert(chatLog.type == ChatLogType.INVITE)
    }

    @Test
    fun `파일 채팅 로그 읽기 - 존재함`() {
        val messageId = "testMessageId4"
        val chatRoomId = "testRoomId4"
        val chatMessage = ChatMessageProvider.buildFileMessage(messageId, chatRoomId)
        mongoDataGenerator.chatLogEntityData(chatMessage)
        val chatLog = chatLogRepositoryImpl.readChatMessage(messageId) as ChatFileLog
        assert(chatLog.messageId == messageId)
        assert(chatLog.chatRoomId == chatRoomId)
        assert(chatLog.senderId == chatMessage.senderId)
        assert(chatLog.number.sequenceNumber == chatMessage.number.sequenceNumber)
        assert(chatLog.number.page == chatMessage.number.page)
        assert(chatLog.timestamp.truncatedTo(ChronoUnit.MILLIS).equals(chatMessage.timestamp.truncatedTo(ChronoUnit.MILLIS)));
        assert(chatLog.medias[0].url == chatMessage.medias[0].url)
        assert(chatLog.type == ChatLogType.FILE)
    }

    @Test
    fun `답장 채팅 로그 읽기 - 존재함`() {
        val messageId = "testMessageId5"
        val chatRoomId = "testRoomId5"
        val parentMessageId = "testParentMessageId"
        val parentLog = ChatMessageProvider.buildNormalLog(parentMessageId, chatRoomId)
        val chatMessage = ChatMessageProvider.buildReplyMessage(messageId, chatRoomId, parentLog)
        mongoDataGenerator.chatLogEntityData(chatMessage)
        val chatLog = chatLogRepositoryImpl.readChatMessage(messageId) as ChatReplyLog
        assert(chatLog.messageId == messageId)
        assert(chatLog.chatRoomId == chatRoomId)
        assert(chatLog.senderId == chatMessage.senderId)
        assert(chatLog.number.sequenceNumber == chatMessage.number.sequenceNumber)
        assert(chatLog.number.page == chatMessage.number.page)
        assert(chatLog.timestamp.truncatedTo(ChronoUnit.MILLIS).equals(chatMessage.timestamp.truncatedTo(ChronoUnit.MILLIS)));
        assert(chatLog.text == chatMessage.text)
        assert(chatLog.type == ChatLogType.REPLY)
        assert(chatLog.parentMessageId == parentMessageId)
        assert(chatLog.parentMessagePage == parentLog.number.page)
        assert(chatLog.parentMessageText == parentLog.text)
        assert(chatLog.parentSeqNumber == parentLog.number.sequenceNumber)
        assert(chatLog.parentMessageType == parentLog.type)
    }

    @Test
    fun `폭탄 채팅 읽기 - 존재함`(){
        val messageId = "testMessageId16"
        val chatRoomId = "testRoomId16"
        val chatMessage = ChatMessageProvider.buildBombMessage(messageId, chatRoomId)
        mongoDataGenerator.chatLogEntityData(chatMessage)
        val chatLog = chatLogRepositoryImpl.readChatMessage(messageId) as ChatBombLog
        assert(chatLog.messageId == messageId)
        assert(chatLog.chatRoomId == chatRoomId)
        assert(chatLog.senderId == chatMessage.senderId)
        assert(chatLog.text == chatMessage.text)
        assert(chatLog.number.sequenceNumber == chatMessage.number.sequenceNumber)
        assert(chatLog.number.page == chatMessage.number.page)
        assert(chatLog.timestamp.truncatedTo(ChronoUnit.MILLIS).equals(chatMessage.timestamp.truncatedTo(ChronoUnit.MILLIS)));
        assert(chatLog.type == ChatLogType.BOMB)
        assert(chatLog.expiredAt.truncatedTo(ChronoUnit.MILLIS).equals(chatMessage.expiredAt.truncatedTo(ChronoUnit.MILLIS)));
    }

    @Test
    fun `채팅로그 리스트 읽기`(){
        val chatRoomId = "testRoomId6"
        val chatMessage1 = ChatMessageProvider.buildNormalMessage("testMessageId6", chatRoomId)
        val chatMessage2 = ChatMessageProvider.buildLeaveMessage("testMessageId7", chatRoomId)
        val chatMessage3 = ChatMessageProvider.buildInviteMessage("testMessageId8", chatRoomId)
        val chatMessage4 = ChatMessageProvider.buildFileMessage("testMessageId9", chatRoomId)
        val chatMessage5 = ChatMessageProvider.buildReplyMessage("testMessageId10", chatRoomId, ChatMessageProvider.buildNormalLog("testParentMessageId", chatRoomId))
        mongoDataGenerator.chatLogEntityData(chatMessage1)
        mongoDataGenerator.chatLogEntityData(chatMessage2)
        mongoDataGenerator.chatLogEntityData(chatMessage3)
        mongoDataGenerator.chatLogEntityData(chatMessage4)
        mongoDataGenerator.chatLogEntityData(chatMessage5)
        val chatLogs = chatLogRepositoryImpl.readChatMessages(chatRoomId, 1)
        assert(chatLogs.size == 5)
        assert(chatLogs[0] is ChatNormalLog)
        assert(chatLogs[1] is ChatLeaveLog)
        assert(chatLogs[2] is ChatInviteLog)
        assert(chatLogs[3] is ChatFileLog)
        assert(chatLogs[4] is ChatReplyLog)
    }


    @Test
    fun `일반 채팅로그 삭제`(){
        val messageId = "testMessageId11"
        val chatRoomId = "testRoomId7"
        val chatMessage = ChatMessageProvider.buildNormalMessage(messageId, chatRoomId)
        mongoDataGenerator.chatLogEntityData(chatMessage)
        chatLogRepositoryImpl.removeMessage(messageId)
        val chatLog = chatLogRepositoryImpl.readChatMessage(messageId)
        assert(chatLog!!.type == ChatLogType.DELETE)
    }

    @Test
    fun `채팅방 나감 채팅로그 삭제`(){
        val messageId = "testMessageId12"
        val chatRoomId = "testRoomId8"
        val chatMessage = ChatMessageProvider.buildLeaveMessage(messageId, chatRoomId)
        mongoDataGenerator.chatLogEntityData(chatMessage)
        chatLogRepositoryImpl.removeMessage(messageId)
        val chatLog = chatLogRepositoryImpl.readChatMessage(messageId)
        assert(chatLog!!.type == ChatLogType.DELETE)
    }

    @Test
    fun `채팅방 초대 채팅로그 삭제`(){
        val messageId = "testMessageId13"
        val chatRoomId = "testRoomId9"
        val chatMessage = ChatMessageProvider.buildInviteMessage(messageId, chatRoomId)
        mongoDataGenerator.chatLogEntityData(chatMessage)
        chatLogRepositoryImpl.removeMessage(messageId)
        val chatLog = chatLogRepositoryImpl.readChatMessage(messageId)
        assert(chatLog!!.type == ChatLogType.DELETE)
    }

    @Test
    fun `파일 채팅로그 삭제`(){
        val messageId = "testMessageId14"
        val chatRoomId = "testRoomId10"
        val chatMessage = ChatMessageProvider.buildFileMessage(messageId, chatRoomId)
        mongoDataGenerator.chatLogEntityData(chatMessage)
        chatLogRepositoryImpl.removeMessage(messageId)
        val chatLog = chatLogRepositoryImpl.readChatMessage(messageId)
        assert(chatLog!!.type == ChatLogType.DELETE)
    }

    @Test
    fun `답장 채팅로그 삭제`(){
        val messageId = "testMessageId15"
        val chatRoomId = "testRoomId11"
        val parentMessageId = "testParentMessageId"
        val parentLog = ChatMessageProvider.buildNormalLog(parentMessageId, chatRoomId)
        val chatMessage = ChatMessageProvider.buildReplyMessage(messageId, chatRoomId, parentLog)
        mongoDataGenerator.chatLogEntityData(chatMessage)
        chatLogRepositoryImpl.removeMessage(messageId)
        val chatLog = chatLogRepositoryImpl.readChatMessage(messageId)
        assert(chatLog!!.type == ChatLogType.DELETE)
    }

    @Test
    fun `채팅로그 추가`(){
        val chatMessage = ChatMessageProvider.buildNormalMessage("testMessageId16", "testRoomId12")
        chatLogRepositoryImpl.appendChatLog(chatMessage)
        val chatLog = chatLogRepositoryImpl.readChatMessage("testMessageId16") as ChatNormalLog
        assert(chatLog.messageId == chatMessage.messageId)
        assert(chatLog.chatRoomId == chatMessage.chatRoomId)
        assert(chatLog.senderId == chatMessage.senderId)
        assert(chatLog.text == chatMessage.text)
        assert(chatLog.number.sequenceNumber == chatMessage.number.sequenceNumber)
        assert(chatLog.number.page == chatMessage.number.page)
        assert(chatLog.timestamp.truncatedTo(ChronoUnit.MILLIS).equals(chatMessage.timestamp.truncatedTo(ChronoUnit.MILLIS)));
        assert(chatLog.type == ChatLogType.NORMAL)
    }

    @Test
    fun `마지막 메시지 조회`(){
        val chatRoomId = "testRoomId13"
        val chatRoomId2  = "testRoomId14"
        val chatRoomId3 = "testRoomId15"
        val chatMessage1 = ChatMessageProvider.buildNormalMessage("testMessageId17", chatRoomId)
        val chatMessage2 = ChatMessageProvider.buildNormalMessage("testMessageId18", chatRoomId2)
        val chatMessage3 = ChatMessageProvider.buildNormalMessage("testMessageId19", chatRoomId3)
        mongoDataGenerator.chatLogEntityData(chatMessage1)
        mongoDataGenerator.chatLogEntityData(chatMessage2)
        mongoDataGenerator.chatLogEntityData(chatMessage3)
        val chatLog = chatLogRepositoryImpl.readLatestMessages(listOf(chatMessage1.number, chatMessage2.number, chatMessage3.number))
        assert(chatLog.size == 3)
    }
}