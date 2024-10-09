package org.chewing.v1.service

import org.chewing.v1.TestDataFactory
import org.chewing.v1.implementation.user.status.UserStatusAppender
import org.chewing.v1.implementation.user.status.UserStatusReader
import org.chewing.v1.implementation.user.status.UserStatusRemover
import org.chewing.v1.implementation.user.status.UserStatusUpdater
import org.chewing.v1.repository.UserStatusRepository
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
        val userStatus = TestDataFactory.createUserStatus()
        whenever(userStatusRepository.readsUserStatus(userId)).thenReturn(listOf(userStatus))

        val result = assertDoesNotThrow {
            userStatusService.getUserStatuses(userId)
        }

        assert(result.size == 1)
    }
}