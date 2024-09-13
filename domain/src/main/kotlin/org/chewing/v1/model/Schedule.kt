package org.chewing.v1.model

import java.time.LocalDate
import java.time.LocalDateTime

class Schedule private constructor(
    val scheduleId: ScheduleId,
    val scheduleName: String,
    val scheduleStartTime: LocalDateTime,
    val scheduleEndTime: LocalDateTime,
    val notificationTime: LocalDateTime,
    val scheduleText: String
) {
    companion object {
        fun of(
            scheduleId: String,
            scheduleName: String,
            scheduleStartTime: LocalDateTime,
            scheduleEndTime: LocalDateTime,
            notificationTime: LocalDateTime,
            scheduleText: String
        ): Schedule {
            return Schedule(
                ScheduleId.of(scheduleId),
                scheduleName,
                scheduleStartTime,
                scheduleEndTime,
                notificationTime,
                scheduleText
            )
        }

        fun generate(
            scheduleName: String,
            scheduleStartTime: LocalDateTime,
            scheduleEndTime: LocalDateTime,
            notificationTime: LocalDateTime,
            scheduleText: String
        ): Schedule {
            return Schedule(
                ScheduleId.empty(),
                scheduleName,
                scheduleStartTime,
                scheduleEndTime,
                notificationTime,
                scheduleText
            )
        }
    }

    class ScheduleId private constructor(private val scheduleId: String) {
        fun value(): String {
            return scheduleId
        }

        companion object {
            fun empty(): ScheduleId {
                return ScheduleId("")
            }

            fun of(id: String): ScheduleId {
                return ScheduleId(id)
            }
        }
    }


}