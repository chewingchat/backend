package org.chewing.v1.implementation.friend.friendship

import org.chewing.v1.model.friend.FriendShip
import org.chewing.v1.model.friend.FriendSortCriteria
import org.chewing.v1.model.user.AccessStatus
import org.chewing.v1.repository.friend.FriendShipRepository
import org.springframework.stereotype.Component

@Component
class FriendShipReader(
    private val friendShipRepository: FriendShipRepository,
) {
    fun readsAccess(userId: String, sort: FriendSortCriteria): List<FriendShip> {
        return friendShipRepository.readsAccess(userId, AccessStatus.ACCESS, sort)
    }

    fun readsAccessIdIn(friendIds: List<String>, userId: String): List<FriendShip> {
        return friendShipRepository.reads(friendIds, userId, AccessStatus.ACCESS)
    }
}
