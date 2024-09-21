package org.chewing.v1.implementation

import org.chewing.v1.implementation.user.UserAppender
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.implementation.user.UserRemover
import org.chewing.v1.implementation.user.UserUpdater
import org.chewing.v1.model.PushToken
import org.chewing.v1.model.User
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
//나중에 푸시 알림 시해도 사용
class PushTokenProcessor(
    private val userAppender: UserAppender,
    private val userRemover: UserRemover,
) {
    @Transactional
    fun processPushToken(
        user: User, appToken: String,
        device: PushToken.Device
    ) {
        userRemover.removePushToken(device)
        userAppender.appendUserPushToken(user, appToken, device)
    }
}