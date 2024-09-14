package org.chewing.v1.repository

import org.chewing.v1.model.announcement.Announcement

interface AnnouncementRepository {
    fun readAnnouncements(): List<Announcement>
    fun readAnnouncement(announcementId: String): Announcement?
}