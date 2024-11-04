package org.chewing.v1.service

import org.chewing.v1.TestDataFactory
import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.implementation.chat.room.*
import org.chewing.v1.implementation.chat.sequence.ChatFinder
import org.chewing.v1.repository.chat.GroupChatRoomMemberRepository
import org.chewing.v1.repository.chat.ChatRoomRepository
import org.chewing.v1.repository.chat.PersonalChatRoomMemberRepository
import org.chewing.v1.service.chat.RoomService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import org.springframework.dao.OptimisticLockingFailureException

class RoomServiceTest {
    private val chatRoomRepository: ChatRoomRepository = mock()
    private val groupChatRoomMemberRepository: GroupChatRoomMemberRepository = mock()
    private val personalChatRoomMemberRepository: PersonalChatRoomMemberRepository = mock()
    private val chatFinder: ChatFinder = mock()

    private val chatRoomReader: ChatRoomReader =
        ChatRoomReader(chatRoomRepository, groupChatRoomMemberRepository, personalChatRoomMemberRepository)
    private val chatRoomRemover: ChatRoomRemover =
        ChatRoomRemover(groupChatRoomMemberRepository, personalChatRoomMemberRepository)
    private val chatRoomEnricher: ChatRoomEnricher = ChatRoomEnricher()
    private val chatRoomAppender: ChatRoomAppender =
        ChatRoomAppender(groupChatRoomMemberRepository, personalChatRoomMemberRepository, chatRoomRepository)
    private val chatRoomUpdater: ChatRoomUpdater =
        ChatRoomUpdater(groupChatRoomMemberRepository, personalChatRoomMemberRepository)
    private val chatRoomHandler: ChatRoomHandler = ChatRoomHandler(chatRoomUpdater)
    private val chatRoomValidator: ChatRoomValidator = ChatRoomValidator(chatRoomRepository)

    private val roomService: RoomService = RoomService(
        chatRoomReader,
        chatRoomRemover,
        chatRoomEnricher,
        chatRoomAppender,
        chatRoomHandler,
        chatRoomValidator,
        chatFinder
    )

    @Test
    fun `개인 채팅방 삭제`() {
        // given
        val chatRoomIds = listOf("1", "2", "3")
        val userId = "1"

        // when
        roomService.deleteChatRoom(chatRoomIds, userId)

        verify(personalChatRoomMemberRepository).removes(chatRoomIds, userId)
    }

    @Test
    fun `그룹 채팅방 삭제`() {
        // given
        val chatRoomIds = listOf("1", "2", "3")
        val userId = "1"

        // when
        roomService.deleteGroupChatRooms(chatRoomIds, userId)

        verify(groupChatRoomMemberRepository).removes(chatRoomIds, userId)
    }

    @Test
    fun `채팅방 그룹 삭제`() {
        // given
        val chatRoomIds = listOf("1", "2", "3")
        val userId = "1"

        // when
        roomService.deleteGroupChatRooms(chatRoomIds, userId)

        verify(groupChatRoomMemberRepository).removes(chatRoomIds, userId)
    }

    @Test
    fun `채팅방 생성 - 이전에 친구와 만든 채팅방이 존재 하지 않음`() {
        // given
        val userId = "testUserId"
        val friendId = "testFriendId"
        val chatRoomId = "testChatRoomId"

        whenever(personalChatRoomMemberRepository.readIdIfExist(userId, friendId)).thenReturn(null)
        whenever(chatRoomAppender.append(false)).thenReturn(chatRoomId)

        // when
        val result = roomService.createChatRoom(userId, friendId)

        assert(result == chatRoomId)
    }

    @Test
    fun `개인 채팅방 생성 - 기존 채팅방 존재시`() {
        // given
        val userId = "testUserId"
        val friendId = "testFriendId"
        val chatRoomId = "testChatRoomId"
        val number = TestDataFactory.createChatNumber(chatRoomId)

        whenever(personalChatRoomMemberRepository.readIdIfExist(userId, friendId)).thenReturn(chatRoomId)
        whenever(chatFinder.findCurrentNumber(chatRoomId)).thenReturn(number)
        // when
        val result = roomService.createChatRoom(userId, friendId)

        assert(result == chatRoomId)
    }

    @Test
    fun `기존에 개인간의 채팅방에 있어 즐겨찾기시 에러가 발생하는 경우`() {
        // given
        val chatRoomId = "testChatRoomId"
        val userId = "testUserId"
        val chatRoomInfo = TestDataFactory.createChatRoomInfo(chatRoomId)

        whenever(chatRoomRepository.readChatRoom(chatRoomId)).thenReturn(chatRoomInfo)
        whenever(personalChatRoomMemberRepository.updateFavorite(chatRoomId, userId, true)).thenThrow(
            OptimisticLockingFailureException::class.java
        )

        // when
        val exception = assertThrows<ConflictException>() {
            roomService.favoriteChatRoom(chatRoomId, userId, true)
        }


        verify(personalChatRoomMemberRepository, times(5)).updateFavorite(chatRoomId, userId, true)
        assert(exception.errorCode == ErrorCode.CHATROOM_FAVORITE_FAILED)
    }

    @Test
    fun `기존에 개인간의 채팅방에 있어 즐겨찾기시 성공`() {
        // given
        val chatRoomId = "testChatRoomId"
        val userId = "testUserId"
        val chatRoomInfo = TestDataFactory.createChatRoomInfo(chatRoomId)

        whenever(chatRoomRepository.readChatRoom(chatRoomId)).thenReturn(chatRoomInfo)

        assertDoesNotThrow {
            roomService.favoriteChatRoom(chatRoomId, userId, true)
        }
    }

    @Test
    fun `기존에 그룹 채팅방에 있어 즐겨찾기시 에러가 발생하는 경우`() {
        // given
        val chatRoomId = "testChatRoomId"
        val userId = "testUserId"
        val chatRoomInfo = TestDataFactory.createGroupChatRoomInfo(chatRoomId)

        whenever(chatRoomRepository.readChatRoom(chatRoomId)).thenReturn(chatRoomInfo)
        whenever(groupChatRoomMemberRepository.updateFavorite(chatRoomId, userId, true)).thenThrow(
            OptimisticLockingFailureException::class.java
        )

        // when
        val exception = assertThrows<ConflictException>() {
            roomService.favoriteChatRoom(chatRoomId, userId, true)
        }


        verify(groupChatRoomMemberRepository, times(5)).updateFavorite(chatRoomId, userId, true)
        assert(exception.errorCode == ErrorCode.CHATROOM_FAVORITE_FAILED)
    }

    @Test
    fun `기존에 그룹 채팅방에 있어 즐겨찾기시 성공`() {
        // given
        val chatRoomId = "testChatRoomId"
        val userId = "testUserId"
        val chatRoomInfo = TestDataFactory.createGroupChatRoomInfo(chatRoomId)

        whenever(chatRoomRepository.readChatRoom(chatRoomId)).thenReturn(chatRoomInfo)

        assertDoesNotThrow {
            roomService.favoriteChatRoom(chatRoomId, userId, true)
        }
    }

    @Test
    fun `그룹 채팅방 생성`() {
        // given
        val userId = "testUserId"
        val friendIds = listOf("testFriendId1", "testFriendId2")
        val chatRoomId = "testChatRoomId"
        val number = TestDataFactory.createChatNumber(chatRoomId)

        whenever(chatRoomAppender.append(true)).thenReturn(chatRoomId)
        whenever(chatFinder.findCurrentNumber(chatRoomId)).thenReturn(number)
        // when
        val result = roomService.createGroupChatRoom(userId, friendIds)

        verify(groupChatRoomMemberRepository).appends(chatRoomId, friendIds, number)
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
        val personalChatRoomMembers = listOf(
            TestDataFactory.createChatRoomMemberInfo(chatRoomId1, userId, 1, true),
            TestDataFactory.createChatRoomMemberInfo(chatRoomId1, friendId, 3, true),
        )
        val groupChatRoomMembers = listOf(
            TestDataFactory.createChatRoomMemberInfo(chatRoomId2, userId, 5, true),
            TestDataFactory.createChatRoomMemberInfo(chatRoomId2, friendId, 6, true)
        )

        whenever(personalChatRoomMemberRepository.reads(userId)).thenReturn(personalChatRoomMembers)
        whenever(groupChatRoomMemberRepository.reads(userId)).thenReturn(groupChatRoomMembers)
        whenever(chatRoomRepository.readChatRooms(listOf(chatRoomId2, chatRoomId1))).thenReturn(chatRooms)

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
        assert(result[0].startSequenceNumber == 1)
        assert(result[1].chatRoomMemberInfos[0].memberId == userId)
        assert(result[1].chatRoomMemberInfos[0].isOwned)
        assert(result[1].chatRoomMemberInfos[1].memberId == friendId)
        assert(!result[1].chatRoomMemberInfos[1].isOwned)
        assert(result[1].favorite)
        assert(result[1].readSequenceNumber == 5)
        assert(result[1].startSequenceNumber == 5)
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
        val personalChatRoomMembers = listOf(
            TestDataFactory.createChatRoomMemberInfo(chatRoomId2, userId, 2, false),
            TestDataFactory.createChatRoomMemberInfo(chatRoomId1, friendId, 3, true),
            TestDataFactory.createChatRoomMemberInfo(chatRoomId2, friendId, 4, true)
        )


        whenever(personalChatRoomMemberRepository.reads(userId)).thenReturn(personalChatRoomMembers)
        whenever(groupChatRoomMemberRepository.reads(userId)).thenReturn(emptyList())
        whenever(chatRoomRepository.readChatRooms(listOf(chatRoomId2, chatRoomId1))).thenReturn(chatRooms)


        val result = roomService.getChatRooms(userId)

        assert(result.size == 1)
    }

    @Test
    fun `채팅방 가져오기 - 실패`(){
        val chatRoomId = "testChatRoomId"
        whenever(chatRoomRepository.readChatRoom(chatRoomId)).thenReturn(null)

        val exception = assertThrows<NotFoundException> {
            roomService.getChatRoom(chatRoomId)
        }

        assert(exception.errorCode == ErrorCode.CHATROOM_NOT_FOUND)
    }

    @Test
    fun `채팅방 가져오기 - 성공`(){
        val chatRoomId = "testChatRoomId"
        val chatRoomInfo = TestDataFactory.createChatRoomInfo(chatRoomId)
        whenever(chatRoomRepository.readChatRoom(chatRoomId)).thenReturn(chatRoomInfo)

        val result = roomService.getChatRoom(chatRoomId)

        assert(result.chatRoomId == chatRoomId)
    }

    @Test
    fun `채팅방 초대 - 성공`() {
        // given
        val chatRoomId = "testChatRoomId"
        val friendId = "testFriendId"
        val userId = "testUserId"
        val number = TestDataFactory.createChatNumber(chatRoomId)

        whenever(chatRoomRepository.isGroupChatRoom(chatRoomId)).thenReturn(true)
        whenever(chatFinder.findCurrentNumber(chatRoomId)).thenReturn(number)

        assertDoesNotThrow {
            roomService.inviteChatRoom(chatRoomId, friendId, userId)
        }
    }

    @Test
    fun `채팅방 초대 - 채팅방이 그룹 채팅방이 아닌 경우`() {
        // given
        val chatRoomId = "testChatRoomId"
        val friendId = "testFriendId"
        val userId = "testUserId"

        whenever(chatRoomRepository.isGroupChatRoom(chatRoomId)).thenReturn(false)

        // when
        val exception = assertThrows<ConflictException> {
            roomService.inviteChatRoom(chatRoomId, friendId, userId)
        }

        assert(exception.errorCode == ErrorCode.CHATROOM_IS_NOT_GROUP)
    }

    @Test
    fun `개인 채팅방 읽기 업데이트 성공`() {
        // given
        val chatRoomId = "testChatRoomId"
        val userId = "testUserId"
        val chatRoomInfo = TestDataFactory.createChatRoomInfo(chatRoomId)
        val number = TestDataFactory.createChatNumber(chatRoomId)

        whenever(chatRoomRepository.readChatRoom(chatRoomId)).thenReturn(chatRoomInfo)

        assertDoesNotThrow {
            roomService.updateReadChatRoom(chatRoomId, userId, number)
        }

        verify(personalChatRoomMemberRepository).updateRead(userId, number)
    }

    @Test
    fun `그룹 채팅방 읽기 업데이트 성공`() {
        // given
        val chatRoomId = "testChatRoomId"
        val userId = "testUserId"
        val chatRoomInfo = TestDataFactory.createGroupChatRoomInfo(chatRoomId)
        val number = TestDataFactory.createChatNumber(chatRoomId)

        whenever(chatRoomRepository.readChatRoom(chatRoomId)).thenReturn(chatRoomInfo)

        assertDoesNotThrow {
            roomService.updateReadChatRoom(chatRoomId, userId, number)
        }

        verify(groupChatRoomMemberRepository).updateRead(userId, number)
    }

    @Test
    fun `그룹 채팅방 읽기 업데이트 실패`() {
        // given
        val chatRoomId = "testChatRoomId"
        val userId = "testUserId"
        val chatRoomInfo = TestDataFactory.createGroupChatRoomInfo(chatRoomId)
        val number = TestDataFactory.createChatNumber(chatRoomId)

        whenever(chatRoomRepository.readChatRoom(chatRoomId)).thenReturn(chatRoomInfo)
        whenever(
            groupChatRoomMemberRepository.updateRead(
                userId,
                number
            )
        ).thenThrow(OptimisticLockingFailureException::class.java)

        roomService.updateReadChatRoom(chatRoomId, userId, number)

        verify(groupChatRoomMemberRepository, times(5)).updateRead(userId, number)
    }

    @Test
    fun `개인 채팅방 읽기 업데이트 실패`() {
        // given
        val chatRoomId = "testChatRoomId"
        val userId = "testUserId"
        val chatRoomInfo = TestDataFactory.createChatRoomInfo(chatRoomId)
        val number = TestDataFactory.createChatNumber(chatRoomId)

        whenever(chatRoomRepository.readChatRoom(chatRoomId)).thenReturn(chatRoomInfo)
        whenever(personalChatRoomMemberRepository.updateRead(userId, number)).thenThrow(
            OptimisticLockingFailureException::class.java
        )

        roomService.updateReadChatRoom(chatRoomId, userId, number)

        verify(personalChatRoomMemberRepository, times(5)).updateRead(userId, number)
    }

    @Test
    fun `채팅방 활성화 - 개인 채팅방`() {
        // given
        val chatRoomId = "testChatRoomId"
        val userId = "testUserId"
        val chatRoomInfo = TestDataFactory.createChatRoomInfo(chatRoomId)
        val number = TestDataFactory.createChatNumber(chatRoomId)

        whenever(chatRoomRepository.readChatRoom(chatRoomId)).thenReturn(chatRoomInfo)
        whenever(personalChatRoomMemberRepository.readFriend(chatRoomId, userId)).thenReturn(
            TestDataFactory.createChatRoomMemberInfo(chatRoomId, userId, 1, true)
        )

        // when
        val result = roomService.activateChatRoom(chatRoomId, userId, number)

        assert(result.chatRoomId == chatRoomId)
    }

    @Test
    fun `채팅방 활성화 - 그룹 채팅방`() {
        // given
        val chatRoomId = "testChatRoomId"
        val userId = "testUserId"
        val chatRoomInfo = TestDataFactory.createGroupChatRoomInfo(chatRoomId)
        val number = TestDataFactory.createChatNumber(chatRoomId)

        whenever(chatRoomRepository.readChatRoom(chatRoomId)).thenReturn(chatRoomInfo)

        // when
        val result = roomService.activateChatRoom(chatRoomId, userId, number)

        verify(personalChatRoomMemberRepository, never()).appendIfNotExist(any(), any(), any(), any())
        assert(result.chatRoomId == chatRoomId)
    }

    @Test
    fun `개인 채팅방 나를 제외한 맴버 가져오기`(){
        val chatRoomId = "testChatRoomId"
        val userId = "testUserId"
        val friendId = "testFriendId"
        val chatRoomInfo = TestDataFactory.createChatRoomInfo(chatRoomId)

        whenever(personalChatRoomMemberRepository.readFriend(chatRoomId, userId)).thenReturn(
            TestDataFactory.createChatRoomMemberInfo(chatRoomId, friendId, 1, true)
        )

        val result = roomService.getChatRoomFriends(chatRoomId, userId, chatRoomInfo)

        assert(result.size == 1)
        assert(result[0].memberId == friendId)
    }

    @Test
    fun `그룹 채팅방 맴버 가져오기`(){
        val chatRoomId = "testChatRoomId"
        val userId = "testUserId"
        val friendId = "testFriendId"
        val chatRoomInfo = TestDataFactory.createGroupChatRoomInfo(chatRoomId)

        whenever(groupChatRoomMemberRepository.readFriends(chatRoomId, userId)).thenReturn(
            listOf(
                TestDataFactory.createChatRoomMemberInfo(chatRoomId, friendId, 1, true)
            )
        )

        val result = roomService.getChatRoomFriends(chatRoomId, userId, chatRoomInfo)

        assert(result.size == 1)
        assert(result[0].memberId == friendId)
    }
}