package org.chewing.v1.implementation.user

import org.chewing.v1.model.User
import org.chewing.v1.model.media.Media
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Component

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
    fun updateUser(user: User, media: Media) {
        return userRepository.updateProfileImage(user,media)
    }
}