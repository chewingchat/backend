package org.chewing.v1.implementation.user

import org.chewing.v1.implementation.StatusReader
import org.chewing.v1.implementation.emoticon.EmoticonReader
import org.chewing.v1.model.UserStatus
import org.springframework.stereotype.Component

@Component
class UserStatusFinder(
    private val statusReader: StatusReader,
    private val emoticonReader: EmoticonReader,
    private val userStatusFilter: UserStatusFilter
) {
    fun find(userId: String): UserStatus {
        val status = statusReader.readSelectedStatus(userId)
        val emoticon = emoticonReader.readEmoticon(status.emoticonId)
        return UserStatus.of(status.statusId, status.statusName, emoticon, userId)
    }

    fun finds(userIds: List<String>): List<UserStatus> {
        // 상태를 읽어와서 statusId로 매핑
        val statuses = statusReader.readSelectedStatuses(userIds).associateBy { it.statusId }
        // 이모티콘을 읽어와서 emoticonId로 매핑
        val emoticons = emoticonReader.readEmoticons(statuses.values.map { it.emoticonId })

        // 이모티콘에 맞는 상태를 찾아 UserStatus 리스트로 변환
        return emoticons.mapNotNull { emoticon ->
            userStatusFilter.filter(emoticon, statuses.values)
        }
    }

}