package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.model.User
import org.chewing.v1.model.schedule.Schedule
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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserJpaEntity
) {
    companion object {
        fun from(schedule: Schedule): ScheduleJpaEntity {
            return ScheduleJpaEntity(
                schedule.id.value(),
                schedule.name,
                schedule.content,
                schedule.startAt,
                schedule.entAt,
                schedule.notificationAt,
                UserJpaEntity.fromUser(schedule.writer)
            )
        }
    }

    fun toSchedule(): Schedule {
        return Schedule.of(
            scheduleId,
            scheduleName,
            scheduleStartAt,
            scheduleEndAt,
            notificationAt,
            scheduleContent,
            User.empty()
        )
    }
}