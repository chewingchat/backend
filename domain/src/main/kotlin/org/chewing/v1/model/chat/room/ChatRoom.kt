package org.chewing.v1.model.chat.room

import org.chewing.v1.model.chat.member.ChatRoomMember
import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import java.time.LocalDateTime


data class ChatRoom(
    val chatRoomId: String,
    val favorite: Boolean,
    val groupChatRoom: Boolean,
    val latestMessage: String,
    val latestMessageTime: LocalDateTime,
    val totalUnReadMessage: Int,
    val latestSeqNumber: Int,
    val latestPage: Int,
    val chatRoomMemberInfos: List<ChatRoomMember>,
) {
    companion object {
        fun of(
            chatRoomInfo: ChatRoomInfo,
            userChatRoom: ChatRoomMemberInfo,
            chatRoomMembers: List<ChatRoomMember>,
            totalUnReadMessage: Int,
            latestPage: Int,
            latestSeqNumber: Int
        ): ChatRoom {
            return ChatRoom(
                chatRoomId = chatRoomInfo.chatRoomId,
                favorite = userChatRoom.favorite,
                groupChatRoom = chatRoomInfo.isGroup,
                latestMessage = chatRoomInfo.latestMessage,
                latestMessageTime = chatRoomInfo.latestMessageTime,
                totalUnReadMessage = totalUnReadMessage,
                latestPage = latestPage,
                latestSeqNumber = latestSeqNumber,
                chatRoomMemberInfos = chatRoomMembers
            )
        }

    }
}