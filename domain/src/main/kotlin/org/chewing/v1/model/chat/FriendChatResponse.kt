package org.chewing.v1.model.chat


data class FriendChatResponse(
    val friendId: String,
    val friendFirstName: String,
    val friendLastName: String,
    val friendImageUrl: String
)


