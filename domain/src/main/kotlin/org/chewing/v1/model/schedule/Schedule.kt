package org.chewing.v1.model.schedule

import java.time.LocalDateTime

class Schedule(
    val id: ScheduleId,
    val content: ScheduleContent,
    val time: ScheduleTime,
) {
    class ScheduleId private constructor(private val scheduleId: String) {
        fun value(): String {
            return scheduleId
        }

        companion object {
            fun of(id: String): ScheduleId {
                return ScheduleId(id)
            }
        }
    }
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
                ScheduleId.of(scheduleId),
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