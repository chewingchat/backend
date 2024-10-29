package org.chewing.v1.service

import org.chewing.v1.TestDataFactory
import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.implementation.chat.room.*
import org.chewing.v1.repository.chat.ChatRoomMemberRepository
import org.chewing.v1.repository.chat.ChatRoomRepository
import org.chewing.v1.service.chat.RoomService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.dao.OptimisticLockingFailureException

class RoomServiceTest {
    private val chatRoomRepository: ChatRoomRepository = mock()
    private val chatRoomMemberRepository: ChatRoomMemberRepository = mock()

    private val chatRoomReader: ChatRoomReader = ChatRoomReader(chatRoomRepository, chatRoomMemberRepository)
    private val chatRoomRemover: ChatRoomRemover = ChatRoomRemover(chatRoomMemberRepository)
    private val chatRoomEnricher: ChatRoomEnricher = ChatRoomEnricher()
    private val chatRoomAppender: ChatRoomAppender = ChatRoomAppender(chatRoomMemberRepository, chatRoomRepository)
    private val chatRoomUpdater: ChatRoomUpdater = ChatRoomUpdater(chatRoomMemberRepository)
    private val chatRoomHandler: ChatRoomHandler = ChatRoomHandler(chatRoomUpdater)

    private val roomService: RoomService = RoomService(
        chatRoomReader,
        chatRoomRemover,
        chatRoomEnricher,
        chatRoomAppender,
        chatRoomHandler
    )

    @Test
    fun `채팅방 삭제`() {
        // given
        val chatRoomIds = listOf("1", "2", "3")
        val userId = "1"

        // when
        roomService.deleteChatRoom(chatRoomIds, userId)

        verify(chatRoomMemberRepository).removeChatRoomMembers(chatRoomIds, userId)
    }

    @Test
    fun `채팅방 그룹 삭제`() {
        // given
        val chatRoomIds = listOf("1", "2", "3")
        val userId = "1"

        // when
        roomService.deleteGroupChatRooms(chatRoomIds, userId)

        verify(chatRoomMemberRepository).removeChatRoomMembers(chatRoomIds, userId)
    }

    @Test
    fun `채팅방 생성 - 이전에 친구와 만든 채팅방이 존재 하지 않음`() {
        // given
        val userId = "testUserId"
        val friendId = "testFriendId"
        val chatRoomId = "testChatRoomId"

        whenever(chatRoomMemberRepository.readPersonalChatRoomId(userId, friendId)).thenReturn(null)
        whenever(chatRoomAppender.append(false)).thenReturn(chatRoomId)

        // when
        val result = roomService.createChatRoom(userId, friendId)


        verify(chatRoomMemberRepository).appendChatRoomMembers(chatRoomId, listOf(userId, friendId))
        assert(result == chatRoomId)
    }

    @Test
    fun `채팅방 생성 - 이전에 친구와 만든 채팅방이 존재 함`() {
        // given
        val userId = "testUserId"
        val friendId = "testFriendId"
        val chatRoomId = "testChatRoomId"

        whenever(chatRoomMemberRepository.readPersonalChatRoomId(userId, friendId)).thenReturn(chatRoomId)

        // when
        val result = roomService.createChatRoom(userId, friendId)

        verify(chatRoomMemberRepository).updateUnDelete(chatRoomId, userId)
        assert(result == chatRoomId)
    }

    @Test
    fun `기존에 채팅방이 있어 업데이트시 에러가 발생하는 경우`() {
        // given
        val chatRoomId = "testChatRoomId"
        val userId = "testUserId"
        val friendId = "testFriendId"
        whenever(chatRoomMemberRepository.readPersonalChatRoomId(userId, friendId)).thenReturn(chatRoomId)

        whenever(chatRoomMemberRepository.updateUnDelete(chatRoomId, userId)).thenThrow(
            OptimisticLockingFailureException::class.java
        )

        // when
        val exception = assertThrows<ConflictException>() {
            roomService.createChatRoom(userId, friendId)
        }


        verify(chatRoomMemberRepository, times(5)).updateUnDelete(chatRoomId, userId)
        assert(exception.errorCode == ErrorCode.CHATROOM_CREATE_FAILED)
    }

    @Test
    fun `그룹 채팅방 생성`() {
        // given
        val userId = "testUserId"
        val friendIds = listOf("testFriendId1", "testFriendId2")
        val chatRoomId = "testChatRoomId"

        whenever(chatRoomAppender.append(true)).thenReturn(chatRoomId)

        // when
        val result = roomService.createGroupChatRoom(userId, friendIds)

        verify(chatRoomMemberRepository).appendChatRoomMembers(chatRoomId, friendIds)
        assert(result == chatRoomId)
    }

    @Test
    fun `채팅방 목록 가져오기`() {
        val userId = "testUserId"
        val friendId = "testFriendId"
        val chatRoomId1 = "testChatRoomId1"
        val chatRoomId2 = "testChatRoomId2"
        val chatRooms = listOf(
            TestDataFactory.createChatRoomInfo(chatRoomId1),
            TestDataFactory.createGroupChatRoomInfo(chatRoomId2)
        )
        val chatRoomMembers = listOf(
            TestDataFactory.createChatRoomMemberInfo(chatRoomId1, userId, 1, true),
            TestDataFactory.createChatRoomMemberInfo(chatRoomId2, userId, 2, false),
            TestDataFactory.createChatRoomMemberInfo(chatRoomId1, friendId, 3, true),
            TestDataFactory.createChatRoomMemberInfo(chatRoomId2, friendId, 4, true)
        )

        whenever(chatRoomReader.readChatRoomMembersByUserId(userId)).thenReturn(chatRoomMembers)
        whenever(chatRoomReader.reads(chatRoomMembers.map { it.chatRoomId })).thenReturn(chatRooms)

        val result = roomService.getChatRooms(userId)

        assert(result.size == 2)
        assert(result[0].chatRoomId == chatRoomId1)
        assert(result[1].chatRoomId == chatRoomId2)
        assert(!result[0].groupChatRoom)
        assert(result[1].groupChatRoom)
        assert(result[0].chatRoomMemberInfos.size == 2)
        assert(result[1].chatRoomMemberInfos.size == 2)
        assert(result[0].chatRoomMemberInfos[0].memberId == userId)
        assert(result[0].chatRoomMemberInfos[0].isOwned)
        assert(result[0].chatRoomMemberInfos[1].memberId == friendId)
        assert(!result[0].chatRoomMemberInfos[1].isOwned)
        assert(result[0].favorite)
        assert(result[0].readSequenceNumber == 1)
        assert(result[1].chatRoomMemberInfos[0].memberId == userId)
        assert(result[1].chatRoomMemberInfos[0].isOwned)
        assert(result[1].chatRoomMemberInfos[1].memberId == friendId)
        assert(!result[1].chatRoomMemberInfos[1].isOwned)
        assert(!result[1].favorite)
        assert(result[1].readSequenceNumber == 2)
    }

    @Test
    fun `채팅방 목록 가져오기 - 내가 포함되어 있지 않는 채팅방이 존재 한다면 포함된 채팅방만 가져와야함`() {
        val userId = "testUserId"
        val friendId = "testFriendId"
        val chatRoomId1 = "testChatRoomId1"
        val chatRoomId2 = "testChatRoomId2"
        val chatRooms = listOf(
            TestDataFactory.createChatRoomInfo(chatRoomId1),
            TestDataFactory.createGroupChatRoomInfo(chatRoomId2)
        )
        val chatRoomMembers = listOf(
            TestDataFactory.createChatRoomMemberInfo(chatRoomId2, userId, 2, false),
            TestDataFactory.createChatRoomMemberInfo(chatRoomId1, friendId, 3, true),
            TestDataFactory.createChatRoomMemberInfo(chatRoomId2, friendId, 4, true)
        )

        whenever(chatRoomReader.readChatRoomMembersByUserId(userId)).thenReturn(chatRoomMembers)
        whenever(chatRoomReader.reads(chatRoomMembers.map { it.chatRoomId })).thenReturn(chatRooms)

        val result = roomService.getChatRooms(userId)

        assert(result.size == 1)
    }
}