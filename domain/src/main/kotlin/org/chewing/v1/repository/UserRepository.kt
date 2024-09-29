package org.chewing.v1.repository

import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserContent
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Repository

@Repository
interface UserRepository {
    fun readUserById(userId: String): User?
    fun readUsersByIds(userIds: List<String>): List<User>
    fun readByContact(contact: Contact): User?
    fun remove(userId: String)
    fun updateProfileImage(user: User, media: Media)
    fun updateName(userId: String, userName: UserName)
    fun updateContent(userId: String, content: UserContent)
    fun updateContact(userId: String, contact: Contact)
    fun updateAccess(userId: String, userContent: UserContent)
    fun appendSearchHistory(user: User, search: FriendSearch)
    fun readSearchHistory(userId: String): List<FriendSearch>
    fun removePushToken(device: PushToken.Device)
    fun appendPushToken(device: PushToken.Device, appToken: String, user: User)
    fun appendUser(contact: Contact): User
    fun checkContactIsUsedByElse(contact: Contact, userId: String): Boolean
}