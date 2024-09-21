package org.chewing.v1.service

import org.chewing.v1.implementation.schedule.ScheduleAppender
import org.chewing.v1.implementation.schedule.ScheduleReader
import org.chewing.v1.implementation.schedule.ScheduleRemover
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.schedule.*
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val userReader: UserReader,
    private val scheduleAppender: ScheduleAppender,
    private val scheduleRemover: ScheduleRemover,
    private val scheduleReader: ScheduleReader
) {
    fun make(userId: String, scheduleTime: ScheduleTime, scheduleContent: ScheduleContent) {
        val user = userReader.read(userId)
        scheduleAppender.append(scheduleTime, scheduleContent, user)
    }

    fun remove(scheduleId: String) {
        scheduleRemover.remove(scheduleId)
    }

    fun fetches(userId: String, type: ScheduleType): List<Schedule> {
        return scheduleReader.reads(userId, type)
    }
}