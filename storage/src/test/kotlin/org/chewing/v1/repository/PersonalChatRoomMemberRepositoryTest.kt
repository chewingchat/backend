package org.chewing.v1.repository

import org.chewing.v1.config.JpaContextTest
import org.chewing.v1.jpaentity.chat.ChatRoomMemberId
import org.chewing.v1.jparepository.chat.PersonalChatRoomMemberJpaRepository
import org.chewing.v1.model.chat.room.ChatNumber
import org.chewing.v1.repository.chat.PersonalChatRoomMemberRepositoryImpl
import org.chewing.v1.repository.support.JpaDataGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class PersonalChatRoomMemberRepositoryTest : JpaContextTest() {
    @Autowired
    private lateinit var personalChatRoomMemberJpaRepository: PersonalChatRoomMemberJpaRepository

    @Autowired
    private lateinit var jpaDataGenerator: JpaDataGenerator

    private val chatRoomMemberRepositoryImpl: PersonalChatRoomMemberRepositoryImpl by lazy {
        PersonalChatRoomMemberRepositoryImpl(personalChatRoomMemberJpaRepository)
    }

    @Test
    fun `채팅방 상대방 정보를 가져와야함`() {
        val chatRoomId = "chatRoomId"
        val userId = "userId1"
        val friendId = "friendId1"
        val number = ChatNumber.of(chatRoomId, 1, 1)
        jpaDataGenerator.personalChatRoomMemberEntityData(userId, friendId, chatRoomId, number)
        val chatRoomMemberInfo = chatRoomMemberRepositoryImpl.readFriend(chatRoomId, userId)
        assert(chatRoomMemberInfo != null)
        assert(chatRoomMemberInfo!!.memberId == friendId)
        assert(chatRoomMemberInfo.chatRoomId == chatRoomId)
        assert(chatRoomMemberInfo.readSeqNumber == number.sequenceNumber)
        assert(!chatRoomMemberInfo.favorite)
        assert(chatRoomMemberInfo.startSeqNumber == number.sequenceNumber)
    }

    @Test
    fun `채팅방 상대방 정보를 추가해야함 - 기존에 없음`() {
        val chatRoomId = "chatRoomId2"
        val userId = "userId2"
        val friendId = "friendId2"
        val number = ChatNumber.of(chatRoomId, 1, 1)

        // friendId2가 userId2의 채팅방에 속해있지 않은 상태
        chatRoomMemberRepositoryImpl.appendIfNotExist(chatRoomId, userId, friendId, number)
        val result = personalChatRoomMemberJpaRepository.findById(ChatRoomMemberId(chatRoomId, friendId))
        assert(result.isPresent)
        // 친구 입장에서 확인
        assert(result.get().toRoomOwned().memberId == friendId)
        assert(result.get().toRoomOwned().chatRoomId == chatRoomId)
        assert(result.get().toRoomOwned().readSeqNumber == number.sequenceNumber)
        assert(!result.get().toRoomOwned().favorite)
        assert(result.get().toRoomOwned().startSeqNumber == number.sequenceNumber)
    }

    @Test
    fun `채팅방 상대방 정보를 추가해야함 - 기존에 있음`() {
        val chatRoomId = "chatRoomId3"
        val userId = "userId3"
        val friendId = "friendId3"
        val number = ChatNumber.of(chatRoomId, 1, 1)
        val newNumber = ChatNumber.of(chatRoomId, 2, 2)

        // friendId3이 userId3의 채팅방에 속해있는 상태
        jpaDataGenerator.personalChatRoomMemberEntityData(friendId, userId, chatRoomId, number)
        chatRoomMemberRepositoryImpl.appendIfNotExist(chatRoomId, userId, friendId, newNumber)
        val result = personalChatRoomMemberJpaRepository.findById(ChatRoomMemberId(chatRoomId, friendId))
        assert(result.isPresent)
        // 친구 입장에서 확인
        assert(result.get().toRoomOwned().memberId == friendId)
        assert(result.get().toRoomOwned().chatRoomId == chatRoomId)
        assert(result.get().toRoomOwned().readSeqNumber == number.sequenceNumber)
        assert(!result.get().toRoomOwned().favorite)
        // startSeqNumber는 변경되지 않아야 함
        assert(result.get().toRoomFriend().startSeqNumber == number.sequenceNumber)
    }

    @Test
    fun `채팅방을 삭제함`() {
        val chatRoomId = "chatRoomId4"
        val userId = "userId4"
        val friendId = "friendId4"
        val number = ChatNumber.of(chatRoomId, 1, 1)
        jpaDataGenerator.personalChatRoomMemberEntityData(userId, friendId, chatRoomId, number)
        chatRoomMemberRepositoryImpl.removes(listOf(chatRoomId), userId)
        val result = personalChatRoomMemberJpaRepository.findById(ChatRoomMemberId(chatRoomId, friendId))
        assert(result.isEmpty)
    }

    @Test
    fun `채팅방의 모든 유저 가져오기`() {
        val chatRoomId = "chatRoomId5"
        val userId = "userId5"
        val friendId = "friendId5"
        val number = ChatNumber.of(chatRoomId, 1, 1)
        jpaDataGenerator.personalChatRoomMemberEntityData(friendId, userId, chatRoomId, number)
        jpaDataGenerator.personalChatRoomMemberEntityData(userId, friendId, chatRoomId, number)
        val chatRoomMemberInfo = chatRoomMemberRepositoryImpl.reads(userId)
        assert(chatRoomMemberInfo.size == 2)
    }

    @Test
    fun `채팅방에서 좋아요 변경 처리`() {
        val chatRoomId = "chatRoomId6"
        val userId = "userId6"
        val friendId = "friendId6"
        val number = ChatNumber.of(chatRoomId, 1, 1)
        jpaDataGenerator.personalChatRoomMemberEntityData(userId, friendId, chatRoomId, number)
        chatRoomMemberRepositoryImpl.updateFavorite(chatRoomId, userId, true)
        val result = personalChatRoomMemberJpaRepository.findById(ChatRoomMemberId(chatRoomId, userId))
        assert(result.isPresent)
        assert(result.get().toRoomOwned().favorite)
    }

    @Test
    fun `채팅방에서 유저 읽음 처리`() {
        val chatRoomId = "chatRoomId7"
        val userId = "userId7"
        val friendId = "friendId7"
        val preChatNumber = ChatNumber.of(chatRoomId, 1, 0)
        val chatNumber = ChatNumber.of(chatRoomId, 50, 1)
        jpaDataGenerator.personalChatRoomMemberEntityData(userId, friendId, chatRoomId, preChatNumber)
        chatRoomMemberRepositoryImpl.updateRead(userId, chatNumber)
        val result = personalChatRoomMemberJpaRepository.findById(ChatRoomMemberId(chatRoomId, userId))
        assert(result.isPresent)
        assert(result.get().toRoomOwned().readSeqNumber == chatNumber.sequenceNumber)
    }

    @Test
    fun `채팅방에서 유저 아이디 찾기`() {
        val userId = "userId8"
        val friendId = "friendId8"
        val chatRoomId = "chatRoomId8"
        val number = ChatNumber.of(chatRoomId, 1, 1)
        jpaDataGenerator.personalChatRoomMemberEntityData(userId, friendId, chatRoomId, number)
        val result = chatRoomMemberRepositoryImpl.readIdIfExist(userId, friendId)
        assert(result == chatRoomId)
    }

    @Test
    fun `채팅방 상대방 정보를 가져와야함 - 없음`() {
        val chatRoomId = "chatRoomId9"
        val userId = "userId9"
        val chatRoomMemberInfo = chatRoomMemberRepositoryImpl.readFriend(chatRoomId, userId)
        assert(chatRoomMemberInfo == null)
    }
}
