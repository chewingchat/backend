package org.chewing.v1.dto.request

class UserStatusRequest {
    data class Update(
        val statusId : String = ""
    )
    data class Add(
        val emoticonId: String = "",
        val statusMessage: String = ""
    )
    data class Delete(
        val statusId: String = ""
    )
}