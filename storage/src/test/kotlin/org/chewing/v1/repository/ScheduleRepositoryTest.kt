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
        val user = UserProvider.buildNormal(userId)
        scheduleRepositoryImpl.append(time, content, user)
    }

    @Test
    fun `스케줄 삭제에 성공`() {
        val userId = "userId2"
        val content = ScheduleProvider.buildContent()
        val time = ScheduleProvider.buildTime()
        val user = UserProvider.buildNormal(userId)
        val schedule = testDataGenerator.scheduleEntityData(content, time, user)
        scheduleRepositoryImpl.remove(schedule.id)
        assert(scheduleJpaRepository.findById(schedule.id).isEmpty)
    }

    @Test
    fun `스케줄 전체 삭제에 성공`() {
        val userId = "userId3"
        val content = ScheduleProvider.buildContent()
        val time = ScheduleProvider.buildTime()
        val user = UserProvider.buildNormal(userId)
        val schedule = testDataGenerator.scheduleEntityData(content, time, user)
        scheduleRepositoryImpl.removeUsers(user.userId)
        assert(scheduleJpaRepository.findById(schedule.id).isEmpty)
    }

    @Test
    fun `시작시간 기준으로 스케줄 조회에 성공`() {
        val userId = "userId4"
        val content = ScheduleProvider.buildContent()
        val firstTime = ScheduleProvider.build1000YearFirstTime()
        val secondTime = ScheduleProvider.build1000YearSecondTime()
        val user = UserProvider.buildNormal(userId)
        val schedule2 = testDataGenerator.scheduleEntityData(content, secondTime, user)
        val schedule = testDataGenerator.scheduleEntityData(content, firstTime, user)
        val schedules = scheduleRepositoryImpl.read(user.userId, ScheduleType.of(1000, 1))
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
        val user = UserProvider.buildNormal(userId)
        testDataGenerator.scheduleEntityData(content, secondTime, user)
        testDataGenerator.scheduleEntityData(content, firstTime, user)
        val schedules = scheduleRepositoryImpl.read(user.userId, ScheduleType.of(1001, 1))
        assert(schedules.isEmpty())
    }
}