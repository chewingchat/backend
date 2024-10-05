package org.chewing.v1.repository

import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserContent
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.media.Media
import org.springframework.stereotype.Repository

@Repository
interface UserRepository {
    fun read(userId: String): User?
    fun readContactId(userId: String): Pair<String?, String?>
    fun reads(userIds: List<String>): List<User>
    fun readByContact(contact: Contact): User?
    fun remove(userId: String): String?
    fun updateMedia(user: User, media: Media): String?
    fun updateName(userId: String, userName: UserName): String?
    fun updateContact(userId: String, contact: Contact): String?
    fun updateAccess(userId: String, userContent: UserContent): String?
    fun append(contact: Contact): User
    fun checkContactIsUsedByElse(contact: Contact, userId: String): Boolean
    fun updateBirth(userId: String, birth: String): String?
}