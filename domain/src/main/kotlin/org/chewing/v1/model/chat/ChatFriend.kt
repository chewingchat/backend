package org.chewing.v1.model.chat

data class ChatFriend(  // 이름 변경된 클래스
    val friendId: String,
    val friendFirstName: String,
    val friendLastName: String,
    val friendImageUrl: String
)