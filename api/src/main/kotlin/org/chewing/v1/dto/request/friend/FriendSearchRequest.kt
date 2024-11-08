package org.chewing.v1.dto.request.friend

import org.chewing.v1.model.friend.UserSearch

data class FriendSearchRequest(
    val keyword: String = ""
){
}