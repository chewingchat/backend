package org.chewing.v1.model.chat

data class ChatFriendInfo(  // 이름 변경된 클래스
    // friendId를 통해 나중에 가져오기 -> user service에서 가져오기
    val friendId: Int,
    val chatRoomId: String,
    val readSeqNumber: Int
)