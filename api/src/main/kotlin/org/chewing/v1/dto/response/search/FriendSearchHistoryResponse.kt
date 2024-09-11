package org.chewing.v1.dto.response.search

import org.chewing.v1.model.friend.FriendSearch

data class FriendSearchHistoryResponse(
    val keywords: List<SearchHistoryResponse>
) {
    companion object {
        fun ofList(keywords: List<FriendSearch>): FriendSearchHistoryResponse {
            return FriendSearchHistoryResponse(
                keywords = keywords.map { SearchHistoryResponse.of(it.keyword) }
            )
        }
    }
}