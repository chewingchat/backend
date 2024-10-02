package org.chewing.v1.model.friend

import java.time.LocalDate

class UserSearch private constructor(
    val keyword: String,
    val searchAt: LocalDate?
) {
    companion object {
        fun of(keyword: String, searchTime: LocalDate): UserSearch {
            return UserSearch(keyword, searchTime)
        }

        fun generate(keyword: String): UserSearch {
            return UserSearch(keyword, null)
        }
    }
}