package org.chewing.v1.repository

import org.chewing.v1.jpaentity.ScheduleJpaEntity
import org.chewing.v1.jparepository.ScheduleJpaRepository
import org.chewing.v1.model.User
import org.chewing.v1.model.schedule.*
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters

@Repository
class ScheduleRepositoryImpl(
    private val scheduleJpaRepository: ScheduleJpaRepository
) : ScheduleRepository {
    override fun appendSchedule(scheduleTime: ScheduleTime, scheduleContent: ScheduleContent, writer: User) {
        scheduleJpaRepository.save(ScheduleJpaEntity.generate(scheduleContent, scheduleTime, writer))
    }

    override fun removeSchedule(scheduleId: Schedule.ScheduleId) {
        scheduleJpaRepository.deleteById(scheduleId.value())
    }


    override fun readSchedule(userId: User.UserId, type: ScheduleType): List<Schedule> {
        val startDateTime = LocalDateTime.of(type.year.value, type.month, 1, 0, 0)
        val endDateTime = startDateTime
            .with(TemporalAdjusters.firstDayOfNextMonth()) // 다음 달의 첫 날로 설정
            .minusNanos(1) // 1나노초 전으로 설정하여 현재 월의 마지막 초까지 포함
        return scheduleJpaRepository.findByUserIdAndType(userId.value(), startDateTime, endDateTime)
            .map { it.toScheduleInfo() }
    }
}