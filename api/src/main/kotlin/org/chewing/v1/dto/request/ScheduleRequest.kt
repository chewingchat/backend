package org.chewing.v1.dto.request

import org.chewing.v1.model.schedule.Schedule
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScheduleRequest {
    data class Delete(
        val scheduleId: String = ""
    ){
        fun toScheduleId(): Schedule.ScheduleId {
            return Schedule.ScheduleId.of(scheduleId)
        }
    }
    data class Add(
        val scheduleName: String,
        val scheduleStartTime: String,
        val scheduleEndTime: String,
        val notificationTime: String,
        val scheduleText: String
    ) {
        fun toSchedule(): Schedule {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val startTime = LocalDateTime.parse(scheduleStartTime, formatter)
            val endTime = LocalDateTime.parse(scheduleEndTime, formatter)
            val notification = LocalDateTime.parse(notificationTime, formatter)

            return Schedule.generate(
                scheduleName,
                startTime,
                endTime,
                notification,
                scheduleText
            )
        }
    }
}