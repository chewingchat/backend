package org.chewing.v1.dto.request.chat.message

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ChatBombDto(
    val chatRoomId: String = "",
    val message: String = "",
    val expiredAt: String = ""
) {
    fun toExpireAt(): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")
        return LocalDateTime.parse(expiredAt, formatter)
    }
}
