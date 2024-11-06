package org.chewing.v1.implementation.user.user

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.media.Media
import org.chewing.v1.model.user.UserContent
import org.chewing.v1.model.user.UserName
import org.chewing.v1.repository.user.UserRepository
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
    fun updateFileUrl(userId: String, media: Media): Media {
        return userRepository.updateMedia(userId, media) ?: throw NotFoundException(ErrorCode.USER_NOT_FOUND)
    }

    @Transactional
    fun updateName(userId: String, userName: UserName) {
        userRepository.updateName(userId, userName) ?: throw NotFoundException(ErrorCode.USER_NOT_FOUND)
    }

    @Transactional
    fun updateContact(userId: String, contact: Contact) {
        userRepository.updateContact(userId, contact) ?: throw NotFoundException(ErrorCode.USER_NOT_FOUND)
    }

    @Transactional
    fun updateAccess(userId: String, userContent: UserContent) {
        userRepository.updateAccess(userId, userContent) ?: throw NotFoundException(ErrorCode.USER_NOT_FOUND)
    }

    @Transactional
    fun updateBirth(userId: String, birth: String) {
        userRepository.updateBirth(userId, birth) ?: throw NotFoundException(ErrorCode.USER_NOT_FOUND)
    }
}
