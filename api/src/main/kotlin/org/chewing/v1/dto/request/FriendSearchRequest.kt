package org.chewing.v1.dto.request

import org.chewing.v1.model.friend.UserSearch

data class FriendSearchRequest(
    val keyword: String = ""
){
    fun toSearchFriend(): UserSearch {
        return UserSearch.generate(keyword)
    }
}