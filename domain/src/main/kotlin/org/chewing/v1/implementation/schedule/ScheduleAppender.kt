package org.chewing.v1.implementation.schedule

import org.chewing.v1.model.user.User
import org.chewing.v1.model.schedule.ScheduleContent
import org.chewing.v1.model.schedule.ScheduleTime
import org.chewing.v1.repository.ScheduleRepository
import org.springframework.stereotype.Component

@Component
class ScheduleAppender(
    val scheduleRepository: ScheduleRepository
) {
    fun append(scheduleTime: ScheduleTime, scheduleContent: ScheduleContent, userId: String) {
        scheduleRepository.append(scheduleTime, scheduleContent, userId)
    }
}