package org.chewing.v1.implementation.user

import org.chewing.v1.model.user.StatusInfo
import org.chewing.v1.model.user.UserStatus
import org.chewing.v1.model.emoticon.Emoticon
import org.springframework.stereotype.Component

@Component
class UserStatusFilter {
    fun filter(emoticon: Emoticon, statuses: Collection<StatusInfo>): UserStatus? {
        val status = statuses.firstOrNull { it.emoticonId == emoticon.id }
        return status?.let {
            UserStatus.of(
                it.statusId,
                it.statusName,
                emoticon,
                it.userId
            )
        }
    }
}