package org.chewing.v1.jpaentity.schedule

import jakarta.persistence.*
import org.chewing.v1.model.user.User
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
internal class ScheduleJpaEntity(
    @Id
    private val scheduleId: String = UUID.randomUUID().toString(),
    private val scheduleName: String,
    private val scheduleContent: String,
    private val scheduleStartAt: LocalDateTime,
    private val scheduleEndAt: LocalDateTime,
    private val notificationAt: LocalDateTime,
    private val userId: String,
    private val location: String,
) {
    companion object {
        fun generate(
            scheduleContent: ScheduleContent,
            scheduleTime: ScheduleTime,
            writer: User
        ): ScheduleJpaEntity {
            return ScheduleJpaEntity(
                scheduleName = scheduleContent.title,
                scheduleContent = scheduleContent.memo,
                scheduleStartAt = scheduleTime.startAt,
                scheduleEndAt = scheduleTime.endAt,
                notificationAt = scheduleTime.notificationAt,
                userId = writer.userId,
                location = scheduleContent.location
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
            notificationAt,
            location
        )
    }
}