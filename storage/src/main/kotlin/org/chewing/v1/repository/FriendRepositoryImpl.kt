package org.chewing.v1.repository

import org.chewing.v1.jpaentity.friend.FriendId
import org.chewing.v1.jpaentity.friend.FriendJpaEntity
import org.chewing.v1.jparepository.FriendJpaRepository
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.friend.FriendInfo
import org.springframework.stereotype.Repository

@Repository
internal class FriendRepositoryImpl(
    private val friendJpaRepository: FriendJpaRepository
) : FriendRepository {
    override fun readFriends(userId: String): List<FriendInfo> {
        return friendJpaRepository.findAllByIdUserId(userId).map { it.toFriendInfo() }
    }

    override fun readFriendsByIds(friendIds: List<String>, userId: String): List<FriendInfo> {
        return friendJpaRepository.findAllByIdUserIdInAndIdUserId(friendIds.map { it }, userId)
            .map { it.toFriendInfo() }
    }

    override fun appendFriend(user: User, friendName: UserName, targetUser: User) {
        friendJpaRepository.save(FriendJpaEntity.generate(user, friendName, targetUser))
    }

    override fun removeFriend(userId: String, friendId: String) {
        friendJpaRepository.deleteById(FriendId(userId, friendId))
        friendJpaRepository.deleteById(FriendId(friendId, userId))
    }

    override fun blockFriend(userId: String, friendId: String) {
        friendJpaRepository.findById(FriendId(userId, friendId))?.let {
            it.updateBlock()
            friendJpaRepository.save(it)
        }
    }

    override fun readFriend(userId: String, friendId: String): FriendInfo? {
        return friendJpaRepository.findById(FriendId(userId, friendId))?.toFriendInfo()
    }

    override fun checkFriend(userId: String, friendId: String): Boolean {
        return friendJpaRepository.existsById(FriendId(userId, friendId))
    }

    override fun readFriendShip(userId: String, friendId: String): Pair<FriendInfo, FriendInfo>? {
        val friendIds = listOf(FriendId(userId, friendId), FriendId(friendId, userId))
        val friends = friendJpaRepository.findAllByIdIn(friendIds)

        if (friends.size < 2) return null // 두 관계가 모두 존재해야 함

        val toTarget = friends.find { it.id == FriendId(userId, friendId) }?: return null
        val fromTarget = friends.find { it.id == FriendId(friendId, userId) }?: return null
        return toTarget.toFriendInfo() to fromTarget.toFriendInfo()
    }

    override fun updateFavorite(user: User, friendId: String, favorite: Boolean) {
        friendJpaRepository.findById(FriendId(user.userId, friendId))?.updateFavorite(favorite)
    }

    override fun updateName(user: User, friendId: String, friendName: UserName) {
        friendJpaRepository.findById(FriendId(user.userId, friendId))?.updateName(friendName)
    }

}