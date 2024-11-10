// package org.chewing.v1.implementation
//
// import io.mockk.*
// import kotlinx.coroutines.runBlocking
// import org.chewing.v1.external.ExternalChatNotificationClient
// import org.chewing.v1.external.ExternalPushNotificationClient
// import org.chewing.v1.implementation.notification.NotificationSender
// import org.chewing.v1.model.notification.Notification
// import org.chewing.v1.util.AsyncJobExecutor
// import org.junit.jupiter.api.Test
//
// class NotificationSenderTest {
//    private val externalChatNotificationClient: ExternalChatNotificationClient = mockk()
//    private val asyncJobExecutor: AsyncJobExecutor = mockk()
//    private val externalPushNotificationClient: ExternalPushNotificationClient = mockk()
//
//    private val notificationSender = NotificationSender(
//        externalPushNotificationClient,
//        externalChatNotificationClient,
//        asyncJobExecutor,
//    )
//
//    @Test
//    fun `sendPushNotification with notifications should execute async jobs to send FCM notifications`() {
//        // Arrange: Non-empty notification list
//        val notification1 = mockk<Notification>()
//        val notification2 = mockk<Notification>()
//        val notifications = listOf(notification1, notification2)
//
//        every {
//            asyncJobExecutor.executeAsyncJobs(
//                notifications,
//                any<suspend (Notification) -> Unit>(),
//            )
//        } answers {
//            val items = firstArg<List<Notification>>()
//            val action = secondArg<suspend (Notification) -> Unit>()
//            items.forEach { runBlocking { action(it) } }
//            emptyList<Unit>()
//        }
//
//        coEvery { externalPushNotificationClient.sendFcmNotification(any()) } just Runs
//
//        // Act
//        notificationSender.sendPushNotification(notifications)
//
//        // Assert
//        coVerify(exactly = 1) {
//            asyncJobExecutor.executeAsyncJobs(
//                notifications,
//                any<suspend (Notification) -> Unit>(),
//            )
//        }
//        coVerify(exactly = 1) { externalPushNotificationClient.sendFcmNotification(notification1) }
//        coVerify(exactly = 1) { externalPushNotificationClient.sendFcmNotification(notification2) }
//
//        confirmVerified(asyncJobExecutor, externalPushNotificationClient)
//    }
//
//    @Test
//    fun `sendPushNotification with empty list should not execute any jobs`() {
//        // Arrange: Empty notification list
//        val notifications = emptyList<Notification>()
//
//        coJustRun {
//            asyncJobExecutor.executeAsyncJobs(
//                notifications,
//                any<suspend (Notification) -> Unit>(),
//            )
//        }
//
//        // Act
//        notificationSender.sendPushNotification(notifications)
//
//        // Assert
//        coVerify(exactly = 1) {
//            asyncJobExecutor.executeAsyncJobs(
//                notifications,
//                any<suspend (Notification) -> Unit>(),
//            )
//        }
//        coVerify(exactly = 0) { externalPushNotificationClient.sendFcmNotification(any()) }
//
//        confirmVerified(asyncJobExecutor, externalPushNotificationClient)
//    }
// }
