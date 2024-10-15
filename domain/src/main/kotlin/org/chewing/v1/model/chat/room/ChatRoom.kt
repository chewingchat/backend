package org.chewing.v1.model.chat.room

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.chat.member.ChatRoomMember
import org.chewing.v1.model.chat.member.ChatRoomMemberInfo
import org.chewing.v1.model.chat.message.*
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
            room: Room,
            chatMessage: ChatMessage
        ): ChatRoom {
            when(chatMessage){
                is ChatCommonMessage -> {
                    return ChatRoom(
                        chatRoomId = room.chatRoomId,
                        favorite = room.favorite,
                        groupChatRoom = room.groupChatRoom,
                        latestMessage = chatMessage.text,
                        latestMessageTime = chatMessage.timestamp,
                        totalUnReadMessage = chatMessage.number.sequenceNumber - room.readSequenceNumber,
                        latestSeqNumber = chatMessage.number.sequenceNumber,
                        latestPage = chatMessage.number.page,
                        chatRoomMemberInfos = room.chatRoomMemberInfos
                    )
                }
                is ChatReplyMessage -> {
                    return ChatRoom(
                        chatRoomId = room.chatRoomId,
                        favorite = room.favorite,
                        groupChatRoom = room.groupChatRoom,
                        latestMessage = chatMessage.text,
                        latestMessageTime = chatMessage.timestamp,
                        totalUnReadMessage = chatMessage.number.sequenceNumber - room.readSequenceNumber,
                        latestSeqNumber = chatMessage.number.sequenceNumber,
                        latestPage = chatMessage.number.page,
                        chatRoomMemberInfos = room.chatRoomMemberInfos
                    )
                }
                is ChatFileMessage -> {
                    return ChatRoom(
                        chatRoomId = room.chatRoomId,
                        favorite = room.favorite,
                        groupChatRoom = room.groupChatRoom,
                        latestMessage = chatMessage.medias[0].url,
                        latestMessageTime = chatMessage.timestamp,
                        totalUnReadMessage = chatMessage.number.sequenceNumber - room.readSequenceNumber,
                        latestSeqNumber = chatMessage.number.sequenceNumber,
                        latestPage = chatMessage.number.page,
                        chatRoomMemberInfos = room.chatRoomMemberInfos
                    )
                }
                is ChatInviteMessage -> {
                    return ChatRoom(
                        chatRoomId = room.chatRoomId,
                        favorite = room.favorite,
                        groupChatRoom = room.groupChatRoom,
                        latestMessage = "",
                        latestMessageTime = chatMessage.timestamp,
                        totalUnReadMessage = chatMessage.number.sequenceNumber - room.readSequenceNumber,
                        latestSeqNumber = chatMessage.number.sequenceNumber,
                        latestPage = chatMessage.number.page,
                        chatRoomMemberInfos = room.chatRoomMemberInfos
                    )
                }
                is ChatLeaveMessage -> {
                    return ChatRoom(
                        chatRoomId = room.chatRoomId,
                        favorite = room.favorite,
                        groupChatRoom = room.groupChatRoom,
                        latestMessage = "",
                        latestMessageTime = chatMessage.timestamp,
                        totalUnReadMessage = chatMessage.number.sequenceNumber - room.readSequenceNumber,
                        latestSeqNumber = chatMessage.number.sequenceNumber,
                        latestPage = chatMessage.number.page,
                        chatRoomMemberInfos = room.chatRoomMemberInfos
                    )
                }
                else -> {
                    throw ConflictException(ErrorCode.INTERNAL_SERVER_ERROR)
                }
            }
        }

    }
}