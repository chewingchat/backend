package org.chewing.v1.model.chat.member

class ChatRoomMemberInfo private constructor(
    val memberId: String,
    val chatRoomId: String,
    val readSeqNumber: Int,
    val favorite: Boolean,
){
    companion object{
        fun of(memberId: String, chatRoomId: String, readSeqNumber: Int, favorite: Boolean): ChatRoomMemberInfo {
            return ChatRoomMemberInfo(memberId, chatRoomId, readSeqNumber, favorite)
        }
    }
}