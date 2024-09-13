package org.chewing.v1.dto.response.schedule

import org.chewing.v1.model.Schedule
import java.time.format.DateTimeFormatter

data class ScheduleListResponse(
    val schedules: List<ScheduleResponse>
) {
    companion object {
        fun of(schedules: List<Schedule>): ScheduleListResponse {
            return ScheduleListResponse(
                schedules.map {
                    ScheduleResponse.of(it)
                }
            )
        }
    }

    data class ScheduleResponse(
        val scheduleId: String,
        val scheduleName: String,
        val scheduleStartTime: String,
        val scheduleEndTime: String,
        val notificationTime: String,
        val scheduleText: String
    ) {
        companion object {
            fun of(schedule: Schedule): ScheduleResponse {
                val formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")
                return ScheduleResponse(
                    schedule.scheduleId.value(),
                    schedule.scheduleName,
                    schedule.scheduleStartTime.format(formatter),
                    schedule.scheduleEndTime.format(formatter),
                    schedule.notificationTime.format(formatter),
                    schedule.scheduleText
                )
            }
        }
    }
}
