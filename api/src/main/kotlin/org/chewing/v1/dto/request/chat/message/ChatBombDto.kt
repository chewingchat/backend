package org.chewing.v1.dto.request.chat.message

data class ChatBombDto(
    val chatRoomId: String = "",
    val message: String = "",
    val expiredAt: String = ""
) {
}