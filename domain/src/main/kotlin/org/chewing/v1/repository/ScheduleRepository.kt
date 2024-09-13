package org.chewing.v1.repository

import org.chewing.v1.model.Schedule
import org.chewing.v1.model.ScheduleType
import org.chewing.v1.model.User
import org.springframework.stereotype.Repository

@Repository
interface ScheduleRepository {
    fun appendSchedule(user: User, schedule: Schedule)
    fun removeSchedule(scheduleId: Schedule.ScheduleId)
    fun readSchedule(userId: User.UserId, type: ScheduleType): List<Schedule>
}