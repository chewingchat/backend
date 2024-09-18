package org.chewing.v1.implementation.schedule

import org.chewing.v1.model.User
import org.chewing.v1.model.schedule.ScheduleContent
import org.chewing.v1.model.schedule.ScheduleTime
import org.chewing.v1.repository.ScheduleRepository
import org.springframework.stereotype.Component

@Component
class ScheduleAppender(
    val scheduleRepository: ScheduleRepository
) {
    fun appendSchedule(scheduleTime: ScheduleTime, scheduleContent: ScheduleContent, writer: User) {
        scheduleRepository.appendSchedule(scheduleTime, scheduleContent, writer)
    }
}