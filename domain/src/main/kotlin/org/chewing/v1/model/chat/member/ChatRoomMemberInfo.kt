package org.chewing.v1.model.chat.member

data class ChatRoomMemberInfo(
    val memberId: String,
    val chatRoomId: String,
    val readSeqNumber: Int,
    val favorite: Boolean,
)