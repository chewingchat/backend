package org.chewing.v1.service

import org.chewing.v1.TestDataFactory
import org.chewing.v1.implementation.user.status.UserStatusAppender
import org.chewing.v1.implementation.user.status.UserStatusReader
import org.chewing.v1.implementation.user.status.UserStatusRemover
import org.chewing.v1.implementation.user.status.UserStatusUpdater
import org.chewing.v1.repository.user.UserStatusRepository
import org.chewing.v1.service.user.UserStatusService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UserStatusServiceTest {

    private val userStatusRepository : UserStatusRepository = mock()

    private val userStatusReader : UserStatusReader =  UserStatusReader(userStatusRepository)
    private val userStatusUpdater : UserStatusUpdater = UserStatusUpdater(userStatusRepository)
    private val userStatusRemover : UserStatusRemover = UserStatusRemover(userStatusRepository)
    private val userStatusAppender : UserStatusAppender = UserStatusAppender(userStatusRepository)

    private val userStatusService = UserStatusService(userStatusReader, userStatusUpdater, userStatusRemover, userStatusAppender)


    @Test
    fun `유저의 상태를 선택함 처리`(){
        val userId = "userId"
        val statusId = "statusId"

        assertDoesNotThrow {
            userStatusService.selectUserStatus(userId, statusId)
        }
    }


    @Test
    fun `유저의 상태를 선택 해제`(){
        val userId = "userId"

        assertDoesNotThrow {
            userStatusService.changeSelectUserStatus(userId)
        }
    }

    @Test
    fun `유저의 상태들을 삭제`(){
        val userId = "userId"

        assertDoesNotThrow {
            userStatusService.changeSelectUserStatus(userId)
        }
    }

    @Test
    fun `유저의 상태를 생성`(){
        val userId = "userId"
        val statusMessage = "statusMessage"
        val emoji = "emoji"

        assertDoesNotThrow {
            userStatusService.createUserStatus(userId, statusMessage, emoji)
        }
    }

    @Test
    fun `유저의 모든 상태를 가져온다`(){
        val userId = "userId"
        val userStatus = TestDataFactory.createUserStatus(userId)
        whenever(userStatusRepository.reads(userId)).thenReturn(listOf(userStatus))

        val result = assertDoesNotThrow {
            userStatusService.getUserStatuses(userId)
        }

        assert(result.size == 1)
    }

    @Test
    fun `유저들의 선택한 상태들을 가져온다`(){
        val userId = "userId"
        val userIds = listOf(userId)
        val userStatus = TestDataFactory.createUserStatus(userId)
        whenever(userStatusRepository.readSelectedUsers(userIds)).thenReturn(listOf(userStatus))

        val result = assertDoesNotThrow {
            userStatusService.getSelectedStatuses(userIds)
        }

        assert(result.size == 1)
    }

    @Test
    fun `유저의 모든 상태 목록을 삭제한다`(){
        val userId = "userId"
        val userStatus = TestDataFactory.createUserStatus(userId)
        whenever(userStatusRepository.reads(userId)).thenReturn(listOf(userStatus))

        assertDoesNotThrow {
            userStatusService.deleteAllUserStatuses(userId)
        }
    }

    @Test
    fun `유저의 상태들을 삭제한다`(){
        val statusesId = listOf("statusId")
        assertDoesNotThrow {
            userStatusService.deleteUserStatuses(statusesId)
        }
    }

    @Test
    fun `선택한 상태를 가져온다 - 선택 없음이 아님`(){
        val userId = "userId"
        val userStatus = TestDataFactory.createUserStatus(userId)
        whenever(userStatusRepository.readSelected(userId)).thenReturn(userStatus)

        val result = assertDoesNotThrow {
            userStatusService.getSelectedStatus(userId)
        }

        assert(result == userStatus)
    }

    @Test
    fun `선택한 상태를 가져온다 - 선택 없음`(){
        val userId = "userId"
        val userStatus = TestDataFactory.createDefaultUserStatus()
        whenever(userStatusRepository.readSelected(userId)).thenReturn(userStatus)

        val result = assertDoesNotThrow {
            userStatusService.getSelectedStatus(userId)
        }

        assert(result.statusId == "none")
        assert(result.userId == userId)
        assert(result.message == "none")
        assert(result.emoji == "none")
        assert(result.isSelected)

    }
}