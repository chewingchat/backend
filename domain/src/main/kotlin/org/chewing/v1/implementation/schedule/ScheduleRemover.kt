package org.chewing.v1.implementation.schedule

import org.chewing.v1.model.schedule.Schedule
import org.chewing.v1.repository.ScheduleRepository
import org.springframework.stereotype.Component

@Component
class ScheduleRemover(
    private val scheduleRepository: ScheduleRepository
) {
    fun remove(scheduleId: String) {
        scheduleRepository.removeSchedule(scheduleId)
    }
}