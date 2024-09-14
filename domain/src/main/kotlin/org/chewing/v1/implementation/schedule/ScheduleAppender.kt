package org.chewing.v1.implementation.schedule

import org.chewing.v1.model.schedule.Schedule
import org.chewing.v1.model.User
import org.chewing.v1.repository.ScheduleRepository
import org.springframework.stereotype.Component

@Component
class ScheduleAppender(
    val scheduleRepository: ScheduleRepository
) {
    fun appendSchedule(schedule: Schedule) {
        scheduleRepository.appendSchedule(schedule)
    }
}