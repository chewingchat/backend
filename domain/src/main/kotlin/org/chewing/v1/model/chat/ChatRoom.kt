package org.chewing.v1.model.chat

import org.chewing.v1.model.chat.ChatFriend


data class ChatRoom(
    val chatRoomId: String,
    val favorite: Boolean, // api 명세서 0.2v로 인한 수정
    val groupChatRoom: Boolean, // api 명세서 0.2v로 인한 수정
    val latestMessage: String,
    val latestMessageTime: String,
    val totalUnReadMessage: Int,
    val chatFriends: List<ChatFriend>, // 수정된 부분
    val latestPage: Int,  // 새로 추가된 필드: 마지막 페이지 번호
    val readSeqNumber: Int,  // // api 명세서 0.2v로 인한 수정
)