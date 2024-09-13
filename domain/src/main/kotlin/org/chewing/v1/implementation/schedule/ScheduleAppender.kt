package org.chewing.v1.implementation.schedule

import org.chewing.v1.model.Schedule
import org.chewing.v1.model.User
import org.chewing.v1.repository.ScheduleRepository
import org.springframework.stereotype.Component

@Component
class ScheduleAppender(
    val scheduleRepository: ScheduleRepository
) {
    fun appendSchedule(user: User, schedule: Schedule){
        scheduleRepository.appendSchedule(user, schedule)
    }
}