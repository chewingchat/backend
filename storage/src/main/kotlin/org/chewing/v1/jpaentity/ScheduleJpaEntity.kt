package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.model.User
import org.chewing.v1.model.schedule.ScheduleContent
import org.chewing.v1.model.schedule.Schedule
import org.chewing.v1.model.schedule.ScheduleTime
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "schedule", indexes = [Index(name = "idx_start_at", columnList = "scheduleStartAt"),
        Index(name = "idx_end_at", columnList = "scheduleEndAt")]
)
class ScheduleJpaEntity(
    @Id
    val scheduleId: String = UUID.randomUUID().toString(),
    val scheduleName: String,
    val scheduleContent: String,
    val scheduleStartAt: LocalDateTime,
    val scheduleEndAt: LocalDateTime,
    val notificationAt: LocalDateTime,
    val userId: String
) {
    companion object {
        fun generate(
            scheduleContent: ScheduleContent,
            scheduleTime: ScheduleTime,
            writer: User
        ): ScheduleJpaEntity {
            return ScheduleJpaEntity(
                scheduleName = scheduleContent.title,
                scheduleContent = scheduleContent.text,
                scheduleStartAt = scheduleTime.startAt,
                scheduleEndAt = scheduleTime.endAt,
                notificationAt = scheduleTime.notificationAt,
                userId = writer.userId
            )
        }
    }

    fun toScheduleInfo(): Schedule {
        return Schedule.of(
            scheduleId,
            scheduleName,
            scheduleContent,
            scheduleStartAt,
            scheduleEndAt,
            notificationAt
        )
    }
}