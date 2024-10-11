package org.chewing.v1.model.chat

data class ChatFriend(  // 이름 변경된 클래스
    val friendId: Int,
    val friendFirstName: String,
    val friendLastName: String,
    val imageUrl: String // api 명세서 0.2v로 인한 수정
)