package org.chewing.v1.model.notification

class NotificationAction(
    val actionType: ActionType,
    val targetId: String
){
    companion object {
        fun of(
            actionType: ActionType,
            targetId: String
        ): NotificationAction {
            return NotificationAction(
                actionType = actionType,
                targetId = targetId
            )
        }
    }
    enum class ActionType {
        OPEN_APP,
        VIEW_SCREEN,
        NONE
    }
}
