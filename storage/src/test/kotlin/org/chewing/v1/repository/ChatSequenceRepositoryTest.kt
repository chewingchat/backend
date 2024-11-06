package org.chewing.v1.repository

import org.chewing.v1.config.MongoContextTest
import org.chewing.v1.repository.chat.ChatSequenceRepositoryImpl
import org.chewing.v1.repository.support.MongoDataGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate

class ChatSequenceRepositoryTest : MongoContextTest() {
    @Autowired
    private lateinit var mongoTemplate: MongoTemplate
    @Autowired
    private lateinit var mongoDataGenerator: MongoDataGenerator

    private val chatSequenceRepositoryImpl: ChatSequenceRepositoryImpl by lazy {
        ChatSequenceRepositoryImpl(mongoTemplate)
    }

    @Test
    fun `채팅방 시퀀스 번호 읽기 - 첫 채티방 메시지라면 0 리턴`() {
        val chatRoomId = "testRoomId"
        val result = chatSequenceRepositoryImpl.readCurrent(chatRoomId)
        assert(result.sequenceNumber == 0)
    }

    @Test
    fun `채팅방 시퀀스 번호 읽기 - 채팅방 시퀀스 번호가 1인 경우`() {
        val chatRoomId = "testRoomI2"
        mongoDataGenerator.insertSeqNumber(chatRoomId, 1)
        val result = chatSequenceRepositoryImpl.readCurrent(chatRoomId)
        assert(result.sequenceNumber == 1)
    }

    @Test
    fun `채팅방 목록 시퀀스 번호 읽기 - 모두 처음 인경우`() {
        val chatRoomIds = listOf("testRoomId3", "testRoomId4", "testRoomId5")
        val result = chatSequenceRepositoryImpl.readCurrentSeqNumbers(chatRoomIds)
        assert(result.size == 3)
        assert(result[0].sequenceNumber == 0)
        assert(result[1].sequenceNumber == 0)
        assert(result[2].sequenceNumber == 0)
    }

    @Test
    fun `채팅방 목록 시퀀스 번호 읽기 - 시퀀스가 있는 경우`() {
        val chatRoomIds = listOf("testRoomId6", "testRoomId7", "testRoomId8")
        mongoDataGenerator.insertSeqNumber("testRoomId6", 1)
        mongoDataGenerator.insertSeqNumber("testRoomId7", 1)
        mongoDataGenerator.insertSeqNumber("testRoomId8", 1)
        val result = chatSequenceRepositoryImpl.readCurrentSeqNumbers(chatRoomIds)
        assert(result.size == 3)
        assert(result[0].sequenceNumber == 1)
        assert(result[1].sequenceNumber == 1)
        assert(result[2].sequenceNumber == 1)
    }

    @Test
    fun `채팅방 시퀀스 번호 증가 - 채팅방 시퀀스 번호가 1인 경우`() {
        val chatRoomId = "testRoomId9"
        val result = chatSequenceRepositoryImpl.updateSequenceIncrement(chatRoomId)
        assert(result.sequenceNumber == 1)
    }

    @Test
    fun `채팅방 시퀀스 번호 증가 - 채팅방 시퀀스 번호가 2인 경우`() {
        val chatRoomId = "testRoomId16"
        mongoDataGenerator.insertSeqNumber(chatRoomId, 1)
        val result = chatSequenceRepositoryImpl.updateSequenceIncrement(chatRoomId)
        assert(result.sequenceNumber == 2)
    }

    @Test
    fun `채팅방 목록 시퀀스 번호 증가 - 모두 처음 인경우`() {
        val chatRoomIds = listOf("testRoomId10", "testRoomId11", "testRoomId12")
        val result = chatSequenceRepositoryImpl.updateSequenceIncrements(chatRoomIds)
        assert(result.size == 3)
        assert(result[0].sequenceNumber == 1)
        assert(result[1].sequenceNumber == 1)
        assert(result[2].sequenceNumber == 1)
    }

    @Test
    fun `채팅방 목록 시퀀스 번호 증가 - 시퀀스가 있는 경우`() {
        val chatRoomIds = listOf("testRoomId13", "testRoomId14", "testRoomId15")
        mongoDataGenerator.insertSeqNumber("testRoomId13", 1)
        mongoDataGenerator.insertSeqNumber("testRoomId14", 1)
        mongoDataGenerator.insertSeqNumber("testRoomId15", 1)
        val result = chatSequenceRepositoryImpl.updateSequenceIncrements(chatRoomIds)
        assert(result.size == 3)
        assert(result[0].sequenceNumber == 2)
        assert(result[1].sequenceNumber == 2)
        assert(result[2].sequenceNumber == 2)
    }
}
