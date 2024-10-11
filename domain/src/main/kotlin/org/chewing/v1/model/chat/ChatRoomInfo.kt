package org.chewing.v1.model.chat

class ChatRoomInfo private constructor(
    val chatRoomId: String,
    val isGroupChatRoom: Boolean,
){
    companion object{
        fun of(chatRoomId: String, isGroupChatRoom: Boolean): ChatRoomInfo {
            return ChatRoomInfo(chatRoomId, isGroupChatRoom)
        }
    }
}