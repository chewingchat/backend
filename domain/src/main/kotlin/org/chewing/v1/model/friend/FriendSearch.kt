package org.chewing.v1.model.friend

import java.time.LocalDate

class FriendSearch private constructor(
    val keyword: String,
    val searchTime: LocalDate?
) {
    companion object {
        fun of(keyword: String, searchTime: LocalDate): FriendSearch {
            return FriendSearch(keyword, searchTime)
        }

        fun generate(keyword: String): FriendSearch {
            return FriendSearch(keyword, null)
        }
    }
}