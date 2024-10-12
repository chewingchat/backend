package org.chewing.v1.model.chat.room

import java.time.LocalDateTime

class ChatRoomInfo private constructor(
    val chatRoomId: String,
    val isGroup: Boolean,
    val latestMessage: String,
    val latestMessageTime: LocalDateTime,
    ){
    companion object{
        fun of(
            chatRoomId: String,
            isGroup: Boolean,
            latestMessage: String,
            latestMessageTime: LocalDateTime
        ): ChatRoomInfo {
            return ChatRoomInfo(
                chatRoomId = chatRoomId,
                isGroup = isGroup,
                latestMessage = latestMessage,
                latestMessageTime = latestMessageTime
            )
        }
    }
}