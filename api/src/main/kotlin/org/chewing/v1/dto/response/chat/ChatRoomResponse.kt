package org.chewing.v1.dto.response.chat

import org.chewing.v1.model.chat.ChatRoom

data class ChatRoomResponse(
    val chatRoomId: String,   // 채팅방 ID
    val favorite: Boolean,    // 즐겨찾기 여부
    val groupChatRoom: Boolean, // 그룹 채팅 여부
    val latestMessage: String,  // 마지막 메시지
    val latestMessageTime: String, // 마지막 메시지 시간
    val totalUnReadMessage: Int, // 읽지 않은 메시지 개수
    val chatFriends: List<ChatFriendResponse> // 채팅방 친구 목록
) {
    companion object {
        // ChatRoom을 ChatRoomResponse로 변환하는 함수
        fun from(chatRoom: ChatRoom): ChatRoomResponse {
            return ChatRoomResponse(
                chatRoomId = chatRoom.chatRoomId,
                favorite = chatRoom.favorite,
                groupChatRoom = chatRoom.groupChatRoom,
                latestMessage = chatRoom.latestMessage,
                latestMessageTime = chatRoom.latestMessageTime,
                totalUnReadMessage = chatRoom.totalUnReadMessage,
                chatFriends = chatRoom.chatFriends.map { ChatFriendResponse.from(it) }
            )
        }
    }
}