package org.chewing.v1.implementation.user

import org.chewing.v1.model.PushToken
import org.chewing.v1.model.User
import org.chewing.v1.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserAppender(
    private val userRepository: UserRepository
) {

    fun appendUserPushToken(user: User, pushToken: PushToken) {
        userRepository.appendUserPushToken(user, pushToken)
    }
    //
    // 새로운 유저를 데이터베이스에 저장하는 메서드
    fun appendUser(user: User) {
        userRepository.saveUser(user)
    fun appendSearchedFriend(user: User, search: FriendSearch) {
        return userRepository.appendSearchHistory(user, search)
    }
}