package org.chewing.v1.model.chat

import org.chewing.v1.model.chat.room.ChatRoomInfo
import org.chewing.v1.model.friend.FriendShip
import org.chewing.v1.model.user.User
import java.time.LocalDateTime


data class ChatRoom(
    val chatRoomId: String,
    val favorite: Boolean,
    val groupChatRoom: Boolean,
    val latestMessage: String,
    val latestMessageTime: LocalDateTime,
    val totalUnReadMessage: Int,
    val latestPage: Int,
    val chatRoomMemberInfos: List<ChatRoomMemberInfo>,
) {
    companion object {
        fun of(
            chatRoomInfo: ChatRoomInfo,
            userChatRoom: ChatRoomMemberInfo,
            chatRoomMembers: List<ChatRoomMemberInfo>,
            totalUnReadMessage: Int,
            latestPage: Int,
        ): ChatRoom {
            return ChatRoom(
                chatRoomId = chatRoomInfo.chatRoomId,
                favorite = userChatRoom.favorite,
                groupChatRoom = chatRoomInfo.isGroup,
                latestMessage = chatRoomInfo.latestMessage,
                latestMessageTime = chatRoomInfo.latestMessageTime,
                totalUnReadMessage = totalUnReadMessage,
                latestPage = latestPage,
                chatRoomMemberInfos = chatRoomMembers
            )
        }

    }
}