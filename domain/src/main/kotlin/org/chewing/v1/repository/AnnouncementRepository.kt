package org.chewing.v1.repository

import org.chewing.v1.model.announcement.Announcement

interface AnnouncementRepository {
    fun reads(): List<Announcement>
    fun read(announcementId: String): Announcement?
}