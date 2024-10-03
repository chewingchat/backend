package org.chewing.v1.repository

import org.chewing.v1.config.DbContextTest
import org.chewing.v1.jparepository.PushNotificationJpaRepository
import org.chewing.v1.repository.support.PushTokenProvider
import org.chewing.v1.repository.support.TestDataGenerator
import org.chewing.v1.repository.support.UserProvider
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PushNotificationTest : DbContextTest() {
    @Autowired
    private lateinit var pushNotificationJpaRepository: PushNotificationJpaRepository

    @Autowired
    private lateinit var testDataGenerator: TestDataGenerator

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
        val pushNotification = testDataGenerator.pushNotificationData(userId)
        pushNotificationRepositoryImpl.remove(pushNotification.device)
        assert(pushNotificationJpaRepository.findById(pushNotification.pushTokenId).isEmpty)
    }

    @Test
    fun `푸시 알림 전체 삭제에 성공`() {
        val userId = "userId3"
        val pushNotification = testDataGenerator.pushNotificationData(userId)
        val result = pushNotificationRepositoryImpl.reads(userId)
        assert(result.size == 1)
    }
}