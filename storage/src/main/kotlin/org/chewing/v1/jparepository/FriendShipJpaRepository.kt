package org.chewing.v1.jparepository

import org.chewing.v1.jpaentity.friend.FriendShipId
import org.chewing.v1.jpaentity.friend.FriendShipJpaEntity
import org.chewing.v1.model.AccessStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface FriendShipJpaRepository : JpaRepository<FriendShipJpaEntity, String> {

    fun findById(friendShipId: FriendShipId): FriendShipJpaEntity?
    fun findAllByIdUserIdAndType(userId: String, type: AccessStatus): List<FriendShipJpaEntity>
    fun findAllByIdInAndType(friendShipIds: List<FriendShipId>, type: AccessStatus): List<FriendShipJpaEntity>
}
