package org.chewing.v1.dto.response.chat

import org.chewing.v1.model.chat.room.ChatRoom
import java.time.format.DateTimeFormatter

data class ChatRoomResponse(
    val chatRoomId: String,   // 채팅방 ID
    val favorite: Boolean,    // 즐겨찾기 여부
    val groupChatRoom: Boolean, // 그룹 채팅 여부
    val latestMessage: String,  // 마지막 메시지
    val latestMessageTime: String, // 마지막 메시지 시간
    val totalUnReadMessage: Int, // 읽지 않은 메시지 개수
    val latestPage: Int,
    val latestSeqNumber: Int,
    val members: List<ChatRoomMemberResponse> // 채팅방 친구 목록
) {
    companion object {
        // ChatRoom을 ChatRoomResponse로 변환하는 함수
        fun of(chatRoom: ChatRoom): ChatRoomResponse {
            val formatLatestMessageTime =
                chatRoom.latestMessageTime.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"))
            return ChatRoomResponse(
                chatRoomId = chatRoom.chatRoomId,
                favorite = chatRoom.favorite,
                groupChatRoom = chatRoom.groupChatRoom,
                latestMessage = chatRoom.latestMessage,
                latestMessageTime = formatLatestMessageTime,
                totalUnReadMessage = chatRoom.totalUnReadMessage,
                latestPage = chatRoom.latestPage,
                latestSeqNumber = chatRoom.latestSeqNumber,
                members = chatRoom.chatRoomMemberInfos.map { ChatRoomMemberResponse.from(it) }
            )
        }
    }
}