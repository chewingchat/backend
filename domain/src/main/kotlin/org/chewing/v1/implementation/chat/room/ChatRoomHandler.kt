package org.chewing.v1.implementation.chat.room

import org.chewing.v1.model.chat.room.ChatNumber
import org.chewing.v1.model.feed.FeedTarget
import org.chewing.v1.util.AsyncJobExecutor
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class ChatRoomHandler(
    private val chatRoomUpdater: ChatRoomUpdater,
) {
    fun lockReadChatRoom(userId: String, number: ChatNumber) {
        var retryCount = 0
        val maxRetry = 5
        var delayTime = 100L
        while (retryCount < maxRetry) {
            try {
                chatRoomUpdater.updateRead(userId, number)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(delayTime)
                delayTime *= 2
            }
        }
    }
    fun lockActivateChatRoomUser(chatRoomId: String, userId: String) {
        var retryCount = 0
        val maxRetry = 5
        var delayTime = 100L
        while (retryCount < maxRetry) {
            try {
                chatRoomUpdater.updateUnDelete(chatRoomId, userId)
                return
            } catch (ex: OptimisticLockingFailureException) {
                // 예외 처리: 버전 충돌 시 재시도
                retryCount++
                Thread.sleep(delayTime)
                delayTime *= 2
            }
        }
    }
}