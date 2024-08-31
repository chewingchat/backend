package org.chewing.v1.repository

import org.chewing.v1.jpaentity.FriendJpaEntity
import org.chewing.v1.jparepository.FriendJpaRepository
import org.chewing.v1.model.Friend
import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.User
import org.springframework.stereotype.Repository

@Repository
class FriendRepositoryImpl(
    private val friendJpaRepository: FriendJpaRepository,
) : FriendRepository {
    override fun readFriends(userId: User.UserId): List<Friend> {
        val friends = friendJpaRepository.findAllByUserId(userId.value())
        val friendList = friends.map { it.toFriend() }
        return friendList
    }

    override fun appendFriend(user: User, friend: Friend) {
        friendJpaRepository.save(FriendJpaEntity.fromFriend(user, friend))
    }

    override fun removeFriend(userId: User.UserId, friendId: User.UserId) {
        friendJpaRepository.deleteByUserIdAndFriendId(userId.value(), friendId.value())
    }

    override fun readFriend(userId: User.UserId, friendId: User.UserId): Pair<User, Friend>? {
        val friendEntity = friendJpaRepository.findByUserIdAndFriendId(
            userId.value(),
            friendId.value()
        )

        return friendEntity?.let {
            Pair(it.toUser(), it.toFriend())
        }
    }

    override fun checkFriend(userId: User.UserId, friendId: User.UserId): Boolean {
        return friendJpaRepository.existsByUserIdAndFriendId(userId.value(), friendId.value())
    }

    override fun updateFriend(user: User, friend: Friend) {
        friendJpaRepository.save(FriendJpaEntity.fromFriend(user, friend))
    }
}