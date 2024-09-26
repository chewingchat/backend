package org.chewing.v1.repository

import org.chewing.v1.model.user.User
import org.chewing.v1.model.schedule.*
import org.springframework.stereotype.Repository

@Repository
interface ScheduleRepository {
    fun appendSchedule(scheduleTime: ScheduleTime, scheduleContent: ScheduleContent, writer: User)
    fun removeSchedule(scheduleId: String)
    fun readSchedule(userId: String, type: ScheduleType): List<Schedule>
    fun removeAll(userId: String)
}