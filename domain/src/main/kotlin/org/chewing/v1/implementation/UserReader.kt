package org.chewing.v1.implementation

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.User
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Component

/**
 * UserReader는 사용자 정보를 읽어오는 구현체입니다.
 * 데이터베이스와 캐시 저장소에서 사용자 정보를 읽어오고,
 * 사용자 정보를 캐시에 추가하여 재사용성을 높입니다.
 */
@Component
class UserReader(
    private val userRepository: UserRepository,
) {
    /**
     * 주어진 사용자 ID에 해당하는 사용자 정보를 읽어옵니다.
     * @throws NotFoundException 사용자가 존재하지 않는 경우,
     * USER_NOT_FOUND 오류 코드와 함께 예외를 발생시킵니다.
     */
    fun readUserById(userId: User.UserId): User {
        val user = userRepository.readUserById(userId)
        if (user != null) {
            return user
        } else {
            throw NotFoundException(ErrorCode.USER_NOT_FOUND)
        }
    }
}