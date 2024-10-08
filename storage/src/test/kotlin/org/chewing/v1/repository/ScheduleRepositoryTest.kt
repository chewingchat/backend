package org.chewing.v1.repository

import org.chewing.v1.config.DbContextTest
import org.chewing.v1.jparepository.ScheduleJpaRepository
import org.chewing.v1.model.schedule.ScheduleType
import org.chewing.v1.repository.support.ScheduleProvider
import org.chewing.v1.repository.support.TestDataGenerator
import org.chewing.v1.repository.support.UserProvider
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ScheduleRepositoryTest : DbContextTest() {
    @Autowired
    private lateinit var scheduleJpaRepository: ScheduleJpaRepository

    @Autowired
    private lateinit var testDataGenerator: TestDataGenerator

    private val scheduleRepositoryImpl: ScheduleRepositoryImpl by lazy {
        ScheduleRepositoryImpl(scheduleJpaRepository)
    }

    @Test
    fun `스케줄 저장에 성공`() {
        val userId = "userId"
        val content = ScheduleProvider.buildContent()
        val time = ScheduleProvider.buildTime()
        scheduleRepositoryImpl.append(time, content, userId)
    }

    @Test
    fun `스케줄 삭제에 성공`() {
        val userId = "userId2"
        val content = ScheduleProvider.buildContent()
        val time = ScheduleProvider.buildTime()
      val schedule = testDataGenerator.scheduleEntityData(content, time, userId)
        scheduleRepositoryImpl.remove(schedule.id)
        assert(scheduleJpaRepository.findById(schedule.id).isEmpty)
    }

    @Test
    fun `스케줄 전체 삭제에 성공`() {
        val userId = "userId3"
        val content = ScheduleProvider.buildContent()
        val time = ScheduleProvider.buildTime()
        val schedule = testDataGenerator.scheduleEntityData(content, time, userId)
        scheduleRepositoryImpl.removeUsers(userId)
        assert(scheduleJpaRepository.findById(schedule.id).isEmpty)
    }

    @Test
    fun `시작시간 기준으로 스케줄 조회에 성공`() {
        val userId = "userId4"
        val content = ScheduleProvider.buildContent()
        val firstTime = ScheduleProvider.build1000YearFirstTime()
        val secondTime = ScheduleProvider.build1000YearSecondTime()
        val schedule2 = testDataGenerator.scheduleEntityData(content, secondTime, userId)
        val schedule = testDataGenerator.scheduleEntityData(content, firstTime, userId)
        val schedules = scheduleRepositoryImpl.read(userId, ScheduleType.of(1000, 1))
        assert(schedules.size == 2)
        assert(schedule.time.startAt == schedules[0].time.startAt)
        assert(schedule2.time.startAt == schedules[1].time.startAt)
    }

    @Test
    fun `시작시간 기준으로 스케줄 조회에 실패`() {
        val userId = "userId5"
        val content = ScheduleProvider.buildContent()
        val firstTime = ScheduleProvider.build1000YearFirstTime()
        val secondTime = ScheduleProvider.build1000YearSecondTime()
        testDataGenerator.scheduleEntityData(content, secondTime, userId)
        testDataGenerator.scheduleEntityData(content, firstTime, userId)
        val schedules = scheduleRepositoryImpl.read(userId, ScheduleType.of(1001, 1))
        assert(schedules.isEmpty())
    }
}