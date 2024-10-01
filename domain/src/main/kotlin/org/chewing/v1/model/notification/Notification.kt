package org.chewing.v1.model.notification

import org.chewing.v1.model.auth.PushToken
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.user.User

class Notification private constructor(
    val user: User,
    val pushToken: PushToken,
    val type: NotificationType,
    val action: NotificationAction,
    val imageUrl: String
) {
    companion object {
        fun makeCommentNotification(
            user: User,
            pushToken: PushToken,
            feed: FeedInfo,
        ): Notification {
            return Notification(
                user = user,
                pushToken = pushToken,
                type = NotificationType.COMMENT,
                action = NotificationAction.of(NotificationAction.ActionType.VIEW_SCREEN, feed.feedId),
                imageUrl = ""
            )
        }
    }
}