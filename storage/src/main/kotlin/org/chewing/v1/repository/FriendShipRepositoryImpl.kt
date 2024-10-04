package org.chewing.v1.repository

import org.chewing.v1.jpaentity.friend.FriendShipId
import org.chewing.v1.jpaentity.friend.FriendShipJpaEntity
import org.chewing.v1.jparepository.FriendShipJpaRepository
import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.friend.FriendShip
import org.springframework.stereotype.Repository

@Repository
internal class FriendShipRepositoryImpl(
    private val friendShipJpaRepository: FriendShipJpaRepository
) : FriendShipRepository {
    override fun reads(userId: String, accessStatus: AccessStatus): List<FriendShip> {
        return friendShipJpaRepository.findAllByIdUserIdAndType(userId, accessStatus).map { it.toFriendShip() }
    }

    override fun readsByIds(
        friendIds: List<String>,
        userId: String,
        accessStatus: AccessStatus
    ): List<FriendShip> {
        return friendShipJpaRepository.findAllByIdInAndType(friendIds.map { FriendShipId(userId, it) }, accessStatus)
            .map { it.toFriendShip() }
    }

    override fun append(user: User, friendName: UserName, targetUser: User) {
        friendShipJpaRepository.save(FriendShipJpaEntity.generate(user, friendName, targetUser))
    }

    override fun remove(userId: String, friendId: String) {
        friendShipJpaRepository.findById(FriendShipId(userId, friendId))?.let {
            it.updateDelete()
            friendShipJpaRepository.save(it)
        }
        friendShipJpaRepository.findById(FriendShipId(friendId, userId))?.let {
            it.updateDelete()
            friendShipJpaRepository.save(it)
        }
    }

    override fun block(userId: String, friendId: String) {
        friendShipJpaRepository.findById(FriendShipId(userId, friendId))?.let {
            it.updateBlock()
            friendShipJpaRepository.save(it)
        }
        friendShipJpaRepository.findById(FriendShipId(friendId, userId))?.let {
            it.updateBlocked()
            friendShipJpaRepository.save(it)
        }
    }

    override fun read(userId: String, friendId: String): FriendShip? {
        val friendShip = friendShipJpaRepository.findById(FriendShipId(userId, friendId))
        return friendShip?.toFriendShip()
    }

    override fun updateFavorite(userId: String, friendId: String, favorite: Boolean) {
        friendShipJpaRepository.findById(FriendShipId(userId, friendId))?.updateFavorite(favorite)
    }

    override fun updateName(userId: String, friendId: String, friendName: UserName) {
        friendShipJpaRepository.findById(FriendShipId(userId, friendId))?.updateName(friendName)
    }
}