package org.chewing.v1.repository.announcement

import org.chewing.v1.jparepository.announcement.AnnouncementJpaRepository
import org.chewing.v1.model.announcement.Announcement
import org.springframework.stereotype.Repository

@Repository
internal class AnnouncementRepositoryImpl(
    private val announcementJpaRepository: AnnouncementJpaRepository
) : AnnouncementRepository {
    override fun reads(): List<Announcement> {
        return announcementJpaRepository.findByOrderByCreatedAt().map {
            it.toAnnouncement()
        }
    }

    override fun read(announcementId: String): Announcement? {
        return announcementJpaRepository.findByAnnouncementId(announcementId)?.toAnnouncement()
    }
}
