package org.chewing.v1.dto.request

import org.chewing.v1.model.schedule.Schedule
import org.chewing.v1.model.schedule.ScheduleContent
import org.chewing.v1.model.schedule.ScheduleTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScheduleRequest {
    data class Delete(
        val scheduleId: String = ""
    ){
        fun toScheduleId(): String {
            return scheduleId
        }
    }
    data class Add(
        val scheduleName: String,
        val scheduleStartTime: String,
        val scheduleEndTime: String,
        val notificationTime: String,
        val scheduleText: String
    ) {
        fun toScheduleContent(): ScheduleContent {
            return ScheduleContent.of(scheduleName, scheduleText)
        }
        fun toScheduleTime(): ScheduleTime {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val startTime = LocalDateTime.parse(scheduleStartTime, formatter)
            val endTime = LocalDateTime.parse(scheduleEndTime, formatter)
            val notification = LocalDateTime.parse(notificationTime, formatter)
            return ScheduleTime.of(startTime, endTime, notification)
        }
    }
}