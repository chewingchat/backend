package org.chewing.v1.repository

import org.chewing.v1.model.User
import org.chewing.v1.model.schedule.*
import org.springframework.stereotype.Repository

@Repository
interface ScheduleRepository {
    fun appendSchedule(scheduleTime: ScheduleTime, scheduleContent: ScheduleContent, writer: User)
    fun removeSchedule(scheduleId: Schedule.ScheduleId)
    fun readSchedule(userId: User.UserId, type: ScheduleType): List<Schedule>
}