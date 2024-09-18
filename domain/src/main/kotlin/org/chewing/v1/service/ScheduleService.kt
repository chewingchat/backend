package org.chewing.v1.service

import org.chewing.v1.implementation.schedule.ScheduleAppender
import org.chewing.v1.implementation.schedule.ScheduleReader
import org.chewing.v1.implementation.schedule.ScheduleRemover
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.User
import org.chewing.v1.model.schedule.*
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val userReader: UserReader,
    private val scheduleAppender: ScheduleAppender,
    private val scheduleRemover: ScheduleRemover,
    private val scheduleReader: ScheduleReader
) {
    fun addSchedule(userId: User.UserId, scheduleTime: ScheduleTime, scheduleContent: ScheduleContent) {
        val user = userReader.readUser(userId)
        scheduleAppender.appendSchedule(scheduleTime, scheduleContent, user)
    }

    fun removeSchedule(scheduleId: Schedule.ScheduleId) {
        scheduleRemover.removeSchedule(scheduleId)
    }

    fun getSchedules(userId: User.UserId, type: ScheduleType): List<Schedule> {
        return scheduleReader.readSchedules(userId, type)
    }
}