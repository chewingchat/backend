package org.chewing.v1.jpaentity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.announcement.Announcement
import org.hibernate.annotations.DynamicInsert
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Retry.Topic
import java.util.UUID

@Entity
@DynamicInsert
@Table(name = "announcement", schema = "chewing")
internal class AnnouncementJpaEntity(
    @Id
    val announcementId: String = UUID.randomUUID().toString(),
    val announcementTopic: String,
    val announcementContent: String
) : BaseEntity() {
    fun toAnnouncement(): Announcement {
        return Announcement.of(
            announcementId,
            announcementTopic,
            createdAt!!,
            announcementContent
        )
    }
}