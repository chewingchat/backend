package org.chewing.v1.implementation.user

import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.user.User
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