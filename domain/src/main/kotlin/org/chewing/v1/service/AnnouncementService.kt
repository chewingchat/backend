package org.chewing.v1.service

import org.chewing.v1.implementation.announcement.AnnouncementReader
import org.chewing.v1.model.announcement.Announcement
import org.springframework.stereotype.Service

@Service
class AnnouncementService(
    private val announcementReader: AnnouncementReader
) {
    fun readAnnouncements(): List<Announcement> {
        return announcementReader.readAnnouncements()
    }

    fun readAnnouncement(announcementId: String): Announcement {
        return announcementReader.readAnnouncement(announcementId)
    }
}