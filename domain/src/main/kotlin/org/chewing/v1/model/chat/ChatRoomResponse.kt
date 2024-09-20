package org.chewing.v1.model.chat

import org.chewing.v1.model.chat.FriendChatResponse


data class ChatRoomResponse(
    val chatRoomId: String,
    val isFavorite: Boolean,
    val isGroupChatRoom: Boolean,
    val latestMessage: String,
    val latestMessageTime: String,
    val totalUnreadMessages: Int,
    val friends: List<FriendChatResponse>
)