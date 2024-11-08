package org.chewing.v1.model.chat.room

import java.time.LocalDateTime

class ChatRoomInfo private constructor(
    val chatRoomId: String,
    val isGroup: Boolean,
    ){
    companion object{
        fun of(
            chatRoomId: String,
            isGroup: Boolean,
        ): ChatRoomInfo {
            return ChatRoomInfo(
                chatRoomId = chatRoomId,
                isGroup = isGroup,
            )
        }
    }
}