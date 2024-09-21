package org.chewing.v1.model

class StatusInfo private constructor(
    val statusId: String,
    val statusName: String,
    val userId: String,
    val isSelected: Boolean,
    val emoticonId: String,
) {
    companion object {
        fun of(
            statusId: String,
            statusName: String,
            userId: String,
            isSelected: Boolean,
            emoticonId: String
        ): StatusInfo {
            return StatusInfo(statusId, statusName, userId, isSelected, emoticonId)
        }

        fun default(): StatusInfo {
            return StatusInfo("", "", "", true, "")
        }
    }
}