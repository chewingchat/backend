package org.chewing.v1.repository

import org.chewing.v1.model.friend.UserSearch
import org.chewing.v1.model.user.User

interface UserSearchRepository {
    fun appendSearchHistory(user: User, search: UserSearch)
    fun readSearchHistory(userId: String): List<UserSearch>
}