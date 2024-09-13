package org.chewing.v1.repository

import org.chewing.v1.model.Schedule
import org.chewing.v1.model.ScheduleType
import org.chewing.v1.model.User
import org.springframework.stereotype.Repository

@Repository
class ScheduleRepositoryImpl: ScheduleRepository {
    override fun appendSchedule(user: User, schedule: Schedule) {
        TODO("Not yet implemented")
    }

    override fun removeSchedule(scheduleId: Schedule.ScheduleId) {
        TODO("Not yet implemented")
    }

    override fun readSchedule(userId: User.UserId, type: ScheduleType): List<Schedule> {
        TODO("Not yet implemented")
    }
}