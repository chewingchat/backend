package org.chewing.v1.dto.response.chat

import org.chewing.v1.model.chat.ChatFriend

data class ChatFriendResponse(
    val friendId: Int,   // 친구 ID
    val firstName: String,  // 친구의 이름
    val lastName: String,   // 친구의 성
    val imageUrl: String    // 친구의 프로필 이미지 URL
) {
    companion object {
        // ChatFriend를 ChatFriendResponse로 변환하는 함수
        fun from(chatFriend: ChatFriend): ChatFriendResponse {
            return ChatFriendResponse(
                friendId = chatFriend.friendId,
                firstName = chatFriend.friendFirstName,
                lastName = chatFriend.friendLastName,
                imageUrl = chatFriend.imageUrl
            )
        }
    }
}