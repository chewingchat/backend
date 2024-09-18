package org.chewing.v1.implementation.schedule

import org.chewing.v1.model.schedule.ScheduleType
import org.chewing.v1.model.User
import org.chewing.v1.model.schedule.Schedule
import org.chewing.v1.repository.ScheduleRepository
import org.springframework.stereotype.Component

@Component
class ScheduleReader(
    private val scheduleRepository: ScheduleRepository
) {
    fun readSchedules(userId: User.UserId, type: ScheduleType): List<Schedule> {
        return scheduleRepository.readSchedule(userId, type)
    }
}