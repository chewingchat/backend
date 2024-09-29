package org.chewing.v1.implementation.user

import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserContent
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.media.Media
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * UserUpdater는 사용자 정보를 업데이트하는 구현체입니다.
 * 데이터베이스에서 사용자 정보를 업데이트하고, 캐시에서 해당 사용자의 정보를 제거합니다.
 */
@Component
class UserUpdater(
    private val userRepository: UserRepository,
) {
    /**
     * 주어진 사용자 정보를 업데이트합니다.
     */
    fun updateProfileImage(user: User, media: Media) {
        return userRepository.updateProfileImage(user, media)
    }
    @Transactional
    fun updateName(userId: String, userName: UserName) {
        return userRepository.updateName(userId, userName)
    }
    fun updateContact(userId: String, contact: Contact) {
        return userRepository.updateContact(userId, contact)
    }
    fun updateActivate(userId: String, userContent: UserContent) {
        return userRepository.updateActivate(userId, userContent)
    }
}