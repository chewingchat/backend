package org.chewing.v1.repository

import org.chewing.v1.model.friend.UserSearch
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserContent
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Repository

@Repository
interface UserRepository {
    fun readUserById(userId: String): User?
    fun readContactId(userId: String): Pair<String?, String?>
    fun readUsersByIds(userIds: List<String>): List<User>
    fun readByContact(contact: Contact): User?
    fun remove(userId: String)
    fun updateProfileImage(user: User, media: Media)
    fun updateBackgroundImage(user: User, media: Media)
    fun updateName(userId: String, userName: UserName)
    fun updateContent(userId: String, content: UserContent)
    fun updateContact(userId: String, contact: Contact)
    fun updateAccess(userId: String, userContent: UserContent)
    fun appendUser(contact: Contact): User
    fun checkContactIsUsedByElse(contact: Contact, userId: String): Boolean
    fun updateBirth(userId: String, birth: String)
    fun updateTTS(userId: String, tts: Media): Media?
}