package org.chewing.v1.dto.request

data class FriendFavoriteRequest(
    val friendId: String,
    val favorite: Boolean
)