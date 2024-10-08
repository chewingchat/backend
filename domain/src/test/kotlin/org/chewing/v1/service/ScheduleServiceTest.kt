package org.chewing.v1.service

import org.chewing.v1.TestDataFactory
import org.chewing.v1.implementation.schedule.ScheduleAppender
import org.chewing.v1.implementation.schedule.ScheduleReader
import org.chewing.v1.implementation.schedule.ScheduleRemover
import org.chewing.v1.model.schedule.ScheduleType
import org.chewing.v1.repository.ScheduleRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ScheduleServiceTest {
    private val scheduleRepository: ScheduleRepository = mock()

    private val scheduleAppender = ScheduleAppender(scheduleRepository)
    private val scheduleReader = ScheduleReader(scheduleRepository)
    private val scheduleRemover = ScheduleRemover(scheduleRepository)
    private val scheduleService = ScheduleService(scheduleAppender, scheduleRemover, scheduleReader)

    @Test
    fun `스케줄 추가`() {
        val userId = "userId"
        val scheduleTime = TestDataFactory.createScheduledTime()
        val scheduleContent = TestDataFactory.createScheduleContent()

        assertDoesNotThrow {
            scheduleService.create(userId, scheduleTime, scheduleContent)
        }
    }

    @Test
    fun `스케줄 삭제`() {
        val scheduleId = "scheduleId"

        assertDoesNotThrow {
            scheduleService.remove(scheduleId)
        }
    }

    @Test
    fun `유저 스케줄 삭제`() {
        val userId = "userId"

        assertDoesNotThrow {
            scheduleService.deleteUsers(userId)
        }
    }

    @Test
    fun `스케줄 조회`() {
        val userId = "userId"
        val type = ScheduleType.of(2021, 1)
        val schedule = TestDataFactory.createSchedule()

        whenever(scheduleRepository.read(userId, type)).thenReturn(listOf(schedule))

        val result = assertDoesNotThrow {
            scheduleService.fetches(userId, type)
        }
        
        assert(result.size == 1)
    }
}