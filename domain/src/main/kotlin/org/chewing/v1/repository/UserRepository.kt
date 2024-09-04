package org.chewing.v1.repository

import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.User
import org.springframework.stereotype.Repository

@Repository
interface UserRepository {
    fun readUserById(userId: User.UserId): User?
    fun remove(userId: User.UserId): User.UserId?
    fun updateUser(user: User): User.UserId?
    fun readUserByEmail(email: String): User?
    fun readUserByPhoneNumber(email: String): User?
    fun appendSearchHistory(user: User, search: FriendSearch)
    fun readSearchHistory(userId: User.UserId): List<FriendSearch>
    fun readUserWithStatus(userId: User.UserId): User?
}