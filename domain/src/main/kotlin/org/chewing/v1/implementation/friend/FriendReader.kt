package org.chewing.v1.implementation.friend

import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.friend.FriendShip
import org.chewing.v1.repository.FriendShipRepository
import org.springframework.stereotype.Component

@Component
class FriendReader(
    private val friendShipRepository: FriendShipRepository,
) {
    fun readsAccess(userId: String): List<FriendShip> {
        return friendShipRepository.reads(userId, AccessStatus.ACCESS)
    }

    fun readsAccessIdIn(friendIds: List<String>, userId: String): List<FriendShip> {
        return friendShipRepository.readsByIds(friendIds, userId, AccessStatus.ACCESS)
    }
}