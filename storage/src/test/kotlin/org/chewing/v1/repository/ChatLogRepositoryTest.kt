package org.chewing.v1.repository

import org.chewing.v1.config.MongoContextTest
import org.chewing.v1.mongorepository.ChatLogMongoRepository
import org.chewing.v1.repository.chat.ChatLogRepositoryImpl
import org.chewing.v1.repository.support.ChatMessageProvider
import org.chewing.v1.repository.support.MongoDataGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

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
    fun `채팅 로그 읽기 - 존재함`() {
        val messageId = "testMessageId"
        val chatRoomId = "testRoomId"
        val chatMessage = ChatMessageProvider.buildNormalMessage(messageId, chatRoomId)
        mongoDataGenerator.chatLogEntityData(chatMessage)
        val chatLog = chatLogRepositoryImpl.readChatMessage(messageId)
        assert(chatLog != null)
        assert(chatLog!!.messageId == messageId)
    }
}