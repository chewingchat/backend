package org.chewing.v1.dto.response.announcement

import org.chewing.v1.model.announcement.Announcement
import java.time.format.DateTimeFormatter

data class AnnouncementListResponse(
    val announcements: List<AnnouncementResponse>
) {
    companion object {
        fun of(announcements: List<Announcement>): AnnouncementListResponse {
            return AnnouncementListResponse(
                announcements.map {
                    AnnouncementResponse.of(it)
                }
            )
        }
    }

    data class AnnouncementResponse(
        val announcementId: String,
        val announcementTopic: String,
        val announcementUploadTime: String
    ) {
        companion object {
            fun of(
                announcement: Announcement,
            ): AnnouncementResponse {
                val formattedUploadTime =
                    announcement.uploadAt.format(DateTimeFormatter.ofPattern("yy-MM-dd"))
                return AnnouncementResponse(
                    announcementId = announcement.id,
                    announcementTopic = announcement.topic,
                    announcementUploadTime = formattedUploadTime
                )
            }
        }
    }
}