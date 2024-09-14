package org.chewing.v1.repository

import org.chewing.v1.jparepository.AnnouncementJpaRepository
import org.chewing.v1.model.announcement.Announcement
import org.springframework.stereotype.Repository

@Repository
class AnnouncementRepositoryImpl(
    private val announcementJpaRepository: AnnouncementJpaRepository
) : AnnouncementRepository {
    override fun readAnnouncements(): List<Announcement> {
        return announcementJpaRepository.findByOrderByCreatedAt().map {
            it.toAnnouncement()
        }
    }

    override fun readAnnouncement(announcementId: String): Announcement? {
        return announcementJpaRepository.findByAnnouncementId(announcementId)?.toAnnouncement()
    }
}