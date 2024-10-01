package org.chewing.v1.dto.request

import org.chewing.v1.model.schedule.ScheduleContent
import org.chewing.v1.model.schedule.ScheduleTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScheduleRequest {
    data class Delete(
        val scheduleId: String = ""
    ) {
        fun toScheduleId(): String {
            return scheduleId
        }
    }

    data class Add(
        val title: String,
        val startTime: String,
        val endTime: String,
        val notificationTime: String,
        val memo: String,
        val location: String
    ) {
        fun toScheduleContent(): ScheduleContent {
            return ScheduleContent.of(title, memo, location)
        }

        fun toScheduleTime(): ScheduleTime {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val startTime = LocalDateTime.parse(startTime, formatter)
            val endTime = LocalDateTime.parse(endTime, formatter)
            val notification = LocalDateTime.parse(notificationTime, formatter)
            return ScheduleTime.of(startTime, endTime, notification)
        }
    }
}