package org.chewing.v1.implementation.schedule

import org.chewing.v1.model.User
import org.chewing.v1.model.schedule.Schedule
import org.springframework.stereotype.Component

@Component
class ScheduleEnricher {
    fun enrichSchedule(user: User, schedule: Schedule): Schedule {
        // 일정을 풍부하게 만드는 로직
        return schedule.updateWriter(user)
    }
}
