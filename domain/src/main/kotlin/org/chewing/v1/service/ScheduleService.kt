package org.chewing.v1.service

import org.chewing.v1.implementation.schedule.ScheduleAppender
import org.chewing.v1.implementation.schedule.ScheduleEnricher
import org.chewing.v1.implementation.schedule.ScheduleReader
import org.chewing.v1.implementation.schedule.ScheduleRemover
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.schedule.Schedule
import org.chewing.v1.model.schedule.ScheduleType
import org.chewing.v1.model.User
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val userReader: UserReader,
    private val scheduleAppender: ScheduleAppender,
    private val scheduleRemover: ScheduleRemover,
    private val scheduleReader: ScheduleReader,
    private val scheduleEnricher: ScheduleEnricher
) {
    fun addSchedule(userId: User.UserId, schedule: Schedule) {
        val user = userReader.readUser(userId)
        val enrichSchedule = scheduleEnricher.enrichSchedule(user, schedule)
        scheduleAppender.appendSchedule(enrichSchedule)
    }

    fun removeSchedule(scheduleId: Schedule.ScheduleId) {
        scheduleRemover.removeSchedule(scheduleId)
    }

    fun getSchedules(userId: User.UserId, type: ScheduleType): List<Schedule> {
        return scheduleReader.readSchedules(userId, type)
    }
}