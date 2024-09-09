package org.chewing.v1.implementation

import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.ErrorCode
import org.chewing.v1.model.PushToken
import org.chewing.v1.model.User
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
     * @throws ConflictException 사용자 업데이트에 실패한 경우,
     * USER_UPDATE_FAILED 오류 코드와 함께 예외를 발생시킵니다.
     */
    fun updateUser(user: User): User.UserId {
        return userRepository.updateUser(user) ?: throw ConflictException(ErrorCode.USER_UPDATE_FAILED)
    }

    fun updateUserPushToken(user: User, pushToken: PushToken) {
        userRepository.updateUserPushToken(user, pushToken)
    }
}