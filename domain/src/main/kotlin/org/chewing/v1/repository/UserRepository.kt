package org.chewing.v1.repository

import org.chewing.v1.model.PushToken
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

    //
    fun readPushToken(pushToken: PushToken): PushToken?
    fun appendUserPushToken(user: User, pushToken: PushToken)
    fun updateUserPushToken(user: User, pushToken: PushToken)
    fun saveUser(user: User) // 새로운 유저 정보를 저장하는 메서드

}