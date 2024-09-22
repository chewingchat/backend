package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.AnnouncementJpaEntity
import org.chewing.v1.jpaentity.ScheduleJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.Month
import java.time.Year


@Repository
internal interface ScheduleJpaRepository : JpaRepository<ScheduleJpaEntity, String> {
    @Query(
        "SELECT s FROM ScheduleJpaEntity s WHERE s.userId = :userId AND " +
                "s.scheduleStartAt BETWEEN :startDateTime AND :endDateTime"
    )
    fun findByUserIdAndType(
        userId: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): List<ScheduleJpaEntity>

    fun deleteAllByUserId(userId: String)
}