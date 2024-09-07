package org.chewing.v1.dto.request

import org.chewing.v1.model.friend.FriendSearch

data class FriendSearchRequest(
    val keyword: String = ""
){
    fun toSearchFriend(): FriendSearch {
        return FriendSearch.generate(keyword)
    }
}