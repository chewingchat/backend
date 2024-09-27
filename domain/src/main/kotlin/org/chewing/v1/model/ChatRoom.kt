package org.chewing.v1.model



data class ChatRoom(
    val chatRoomId: String,
    val isFavorite: Boolean,
    val isGroupChatRoom: Boolean,
    val latestMessage: String,
    val latestMessageTime: String,
    val totalUnReadMessage: Int,
    val chatFriends: List<ChatFriend>, // 수정된 부분
    val latestPage: Int,  // 새로 추가된 필드: 마지막 페이지 번호
    val friendReadSeqNumber: Int,  // 새로 추가된 필드: 친구가 읽은 마지막 메시지 시퀀스 번호
)