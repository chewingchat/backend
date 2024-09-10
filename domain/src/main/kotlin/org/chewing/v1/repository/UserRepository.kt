package org.chewing.v1.repository

import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.User
import org.chewing.v1.model.contact.Contact
import org.springframework.stereotype.Repository

@Repository
interface UserRepository {
    fun readUserById(userId: User.UserId): User?
    fun readUserByContact(contact: Contact): User?
    fun remove(userId: User.UserId): User.UserId?
    fun updateUser(user: User): User.UserId?
    fun readUserByEmail(email: String): User?
    fun readUserByPhoneNumber(phoneNumber: String, countryCode: String): User?
    fun appendSearchHistory(user: User, search: FriendSearch)
    fun readSearchHistory(userId: User.UserId): List<FriendSearch>
    fun readUserWithStatus(userId: User.UserId): User?
}