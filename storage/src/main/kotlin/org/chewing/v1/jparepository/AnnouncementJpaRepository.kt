package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.AnnouncementJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface AnnouncementJpaRepository : JpaRepository<AnnouncementJpaEntity, String> {
    fun findByAnnouncementId(announcementId: String): AnnouncementJpaEntity?
    fun findByOrderByCreatedAt(): List<AnnouncementJpaEntity>
}