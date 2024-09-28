package org.chewing.v1.implementation.user

import org.chewing.v1.error.ErrorCode
import org.chewing.v1.error.NotFoundException
import org.chewing.v1.model.ActivateType
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.user.User
import org.chewing.v1.model.contact.Contact
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
    fun read(userId: String): User {
        return userRepository.readUserById(userId)?: throw NotFoundException(ErrorCode.USER_NOT_FOUND)
    }

    fun readByContact(contact: Contact): User {
        return userRepository.readByContact(contact) ?: throw NotFoundException(ErrorCode.USER_NOT_FOUND)
    }

    fun reads(userIds: List<String>): List<User> {
        return userRepository.readUsersByIds(userIds)
    }

    //유저의 최근 친구 검색 목록을 읽어옴
    fun readSearched(userId: String): List<FriendSearch> {
        return userRepository.readSearchHistory(userId)
    }

    fun readOwnEmoticonPacks(userId: String): List<String> {
        return userRepository.readUserEmoticonPacks(userId)
    }
}