package org.chewing.v1.repository

import org.chewing.v1.model.PushToken
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.User
import org.chewing.v1.model.contact.Contact
import org.springframework.stereotype.Repository

@Repository
interface UserRepository {
    fun readUserById(userId: User.UserId): User?
    fun readUserByContact(contact: Contact): User?
    fun remove(userId: User.UserId): User.UserId?
    fun updateUser(user: User)
    fun readUserByEmail(email: String): User?
    fun readUserByPhoneNumber(phoneNumber: String, countryCode: String): User?
    fun appendSearchHistory(user: User, search: FriendSearch)
    fun readSearchHistory(userId: User.UserId): List<FriendSearch>
    fun readUserWithStatus(userId: User.UserId): User?
    fun readUsersWithStatuses(userIds: List<User.UserId>): List<User>
    fun readPushToken(pushToken: PushToken): PushToken?
    fun appendUserPushToken(user: User, pushToken: PushToken)
    fun updateUserPushToken(user: User, pushToken: PushToken)
    fun saveUser(user: User): User.UserId // 새로운 유저 정보를 저장하는 메서드
}