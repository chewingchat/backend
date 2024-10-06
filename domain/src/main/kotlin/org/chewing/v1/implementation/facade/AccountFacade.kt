package org.chewing.v1.implementation.facade

import org.chewing.v1.model.auth.Credential
import org.chewing.v1.model.auth.LoginInfo
import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.service.AuthService
import org.chewing.v1.service.FeedService
import org.chewing.v1.service.ScheduleService
import org.chewing.v1.service.UserService
import org.springframework.stereotype.Service

@Service
class AccountFacade(
    private val authService: AuthService,
    private val userService: UserService,
    private val feedService: FeedService,
    private val scheduleService: ScheduleService
) {
    fun loginAndCreateUser(
        credential: Credential,
        verificationCode: String,
        appToken: String,
        device: PushToken.Device
    ): LoginInfo {
        val contact = authService.verify(credential, verificationCode)
        val user = userService.createUser(contact, appToken, device)
        return authService.createLoginInfo(user)
    }

    fun changeCredential(
        userId: String, credential: Credential, verificationCode: String
    ) {
        val contact = authService.verify(credential,verificationCode)
        userService.updateUserContact(userId,contact)
    }

    fun deleteAccount(userId: String) {
        userService.deleteUser(userId)
        feedService.deleteUsers(userId)
        scheduleService.deleteUsers(userId)
    }
}