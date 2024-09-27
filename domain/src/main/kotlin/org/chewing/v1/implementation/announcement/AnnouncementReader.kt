package org.chewing.v1.implementation.announcement

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.announcement.Announcement
import org.chewing.v1.repository.AnnouncementRepository
import org.springframework.stereotype.Component

@Component
class AnnouncementReader(
    private val announcementRepository: AnnouncementRepository
) {
    fun readAnnouncements(): List<Announcement> {
        return announcementRepository.readAnnouncements()
    }

    fun readAnnouncement(announcementId: String): Announcement {
        return announcementRepository.readAnnouncement(announcementId)
            ?: throw NotFoundException(ErrorCode.ANNOUNCEMENT_NOT_FOUND)
    }
}