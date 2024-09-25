package org.chewing.v1.repository

import org.chewing.v1.jpaentity.friend.FriendId
import org.chewing.v1.jpaentity.friend.FriendJpaEntity
import org.chewing.v1.jparepository.FriendJpaRepository
import org.chewing.v1.jparepository.UserJpaRepository
import org.chewing.v1.jparepository.UserStatusJpaRepository
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.User
import org.chewing.v1.model.UserName
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
        friendJpaRepository.save(FriendJpaEntity.generate(targetUser, user.name, user))
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

    override fun updateFavorite(user: User, friendId: String, favorite: Boolean) {
        friendJpaRepository.findById(FriendId(user.userId, friendId))?.updateFavorite(favorite)
    }

    override fun updateName(user: User, friendId: String, friendName: UserName) {
        friendJpaRepository.findById(FriendId(user.userId, friendId))?.updateName(friendName)
    }

}