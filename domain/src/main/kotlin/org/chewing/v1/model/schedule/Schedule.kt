package org.chewing.v1.model.schedule

import org.chewing.v1.model.User
import java.time.LocalDateTime

class Schedule private constructor(
    val id: ScheduleId,
    val name: String,
    val startAt: LocalDateTime,
    val entAt: LocalDateTime,
    val notificationAt: LocalDateTime,
    val content: String,
    val writer: User
) {
    companion object {
        fun of(
            scheduleId: String,
            scheduleName: String,
            scheduleStartTime: LocalDateTime,
            scheduleEndTime: LocalDateTime,
            notificationTime: LocalDateTime,
            scheduleText: String,
            writer: User
        ): Schedule {
            return Schedule(
                ScheduleId.of(scheduleId),
                scheduleName,
                scheduleStartTime,
                scheduleEndTime,
                notificationTime,
                scheduleText,
                writer
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
                scheduleText,
                User.empty()
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

    fun updateWriter(writer: User): Schedule {
        return Schedule(
            id,
            name,
            startAt,
            entAt,
            notificationAt,
            content,
            writer
        )
    }

}