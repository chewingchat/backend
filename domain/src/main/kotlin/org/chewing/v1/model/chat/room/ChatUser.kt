package org.chewing.v1.model.chat.room

import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.model.user.User

class ChatUser private constructor(
    val user: User,
    val readSequenceNumber: Int,
    val isOwned: Boolean
) {
    companion object {
        fun of(
            user: User,
            chatRoomMemberInfo: ChatRoomMemberInfo,
            userId: String
        ): ChatUser {
            return ChatUser(
                user = user,
                readSequenceNumber = chatRoomMemberInfo.readSeqNumber,
                isOwned = chatRoomMemberInfo.memberId == userId
            )
        }
    }
}