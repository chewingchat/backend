package org.chewing.v1.repository

import org.chewing.v1.config.JpaContextTest
import org.chewing.v1.jparepository.user.PushNotificationJpaRepository
import org.chewing.v1.repository.support.PushTokenProvider
import org.chewing.v1.repository.support.JpaDataGenerator
import org.chewing.v1.repository.support.UserProvider
import org.chewing.v1.repository.user.PushNotificationRepositoryImpl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PushNotificationTest : JpaContextTest() {
    @Autowired
    private lateinit var pushNotificationJpaRepository: PushNotificationJpaRepository

    @Autowired
    private lateinit var jpaDataGenerator: JpaDataGenerator

    private val pushNotificationRepositoryImpl: PushNotificationRepositoryImpl by lazy {
        PushNotificationRepositoryImpl(pushNotificationJpaRepository)
    }

    @Test
    fun `푸시 알림 저장에 성공`() {
        val userId = "userId"
        val user = UserProvider.buildNormal(userId)
        val device = PushTokenProvider.buildDeviceNormal()
        val appToken = PushTokenProvider.buildAppTokenNormal()
        pushNotificationRepositoryImpl.append(device, appToken, user)
        assert(pushNotificationJpaRepository.findAllByUserId(userId).isNotEmpty())
    }

    @Test
    fun `푸시 알림 삭제에 성공`() {
        val userId = "userId2"
        val pushNotification = jpaDataGenerator.pushNotificationData(userId)
        pushNotificationRepositoryImpl.remove(pushNotification.device)
        assert(pushNotificationJpaRepository.findById(pushNotification.pushTokenId).isEmpty)
    }

    @Test
    fun `푸시 알림 전체 삭제에 성공`() {
        val userId = "userId3"
        val pushNotification = jpaDataGenerator.pushNotificationData(userId)
        val result = pushNotificationRepositoryImpl.reads(userId)
        assert(result.size == 1)
    }
}