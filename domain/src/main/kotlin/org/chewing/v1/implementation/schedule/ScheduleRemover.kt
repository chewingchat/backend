package org.chewing.v1.implementation.schedule

import org.chewing.v1.model.Schedule
import org.chewing.v1.repository.ScheduleRepository
import org.springframework.stereotype.Component

@Component
class ScheduleRemover(
    private val scheduleRepository: ScheduleRepository
) {
    fun removeSchedule(scheduleId: Schedule.ScheduleId) {
        scheduleRepository.removeSchedule(scheduleId)
    }
}