package org.chewing.v1.model.schedule

import java.time.LocalDateTime

class Schedule(
    val id: String,
    val content: ScheduleContent,
    val time: ScheduleTime,
) {
    companion object {
        fun of(
            scheduleId: String,
            scheduleTitle: String,
            scheduleText: String,
            scheduleStartTime: LocalDateTime,
            scheduleEndTime: LocalDateTime,
            notificationTime: LocalDateTime,
        ): Schedule {
            return Schedule(
                scheduleId,
                ScheduleContent.of(
                    scheduleTitle,
                    scheduleText,
                ),
                ScheduleTime.of(
                    scheduleStartTime,
                    scheduleEndTime,
                    notificationTime,
                ),
            )
        }
    }
}