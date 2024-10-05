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
    override fun readsOwned(userId: String, accessStatus: AccessStatus): List<FriendShip> {
        return friendShipJpaRepository.findAllByIdUserIdAndType(userId, accessStatus).map { it.toFriendShip() }
    }

    override fun reads(
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

    override fun remove(userId: String, friendId: String): String? {
        return friendShipJpaRepository.findById(FriendShipId(userId, friendId)).map {
            it.updateDelete()
            friendShipJpaRepository.save(it)
            userId
        }.orElse(null)
    }

    override fun block(userId: String, friendId: String): String? {
        return friendShipJpaRepository.findById(FriendShipId(userId, friendId)).map {
            it.updateBlock()
            friendShipJpaRepository.save(it)
            userId
        }.orElse(null)
    }

    override fun blocked(userId: String, friendId: String): String? {
        return friendShipJpaRepository.findById(FriendShipId(userId, friendId)).map {
            it.updateBlocked()
            friendShipJpaRepository.save(it)
            userId
        }.orElse(null)
    }

    override fun read(userId: String, friendId: String): FriendShip? {
        return friendShipJpaRepository.findById(FriendShipId(userId, friendId))
            .orElse(null)?.toFriendShip()
    }

    override fun updateFavorite(userId: String, friendId: String, favorite: Boolean): String? {
        return friendShipJpaRepository.findById(FriendShipId(userId, friendId)).map {
            it.updateFavorite(favorite)
            friendShipJpaRepository.save(it)
            userId
        }.orElse(null)
    }

    override fun updateName(userId: String, friendId: String, friendName: UserName): String? {
        return friendShipJpaRepository.findById(FriendShipId(userId, friendId)).map {
            it.updateName(friendName)
            friendShipJpaRepository.save(it)
            userId
        }.orElse(null)
    }
}