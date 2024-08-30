package org.chewing.v1.repository

import org.chewing.v1.jpaentity.FriendJpaEntity
import org.chewing.v1.jpaentity.UserJpaEntity
import org.chewing.v1.jparepository.FriendJpaRepository
import org.chewing.v1.jparepository.SortJpaRepository
import org.chewing.v1.jparepository.UserJpaRepository
import org.chewing.v1.model.Friend
import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class FriendRepositoryImpl(
    private val friendJpaRepository: FriendJpaRepository,
    private val sortJpaRepository: SortJpaRepository,
) : FriendRepository {
    override fun readFriends(userId: User.UserId): Pair<List<Friend>, SortCriteria> {
        val friends = friendJpaRepository.findAllByUserId(userId.value())
        val sort = sortJpaRepository.findByUserId(userId.value())

        val friendList = friends.map { it.toFriend() }
        val sortCriteria = sort.friendSort

        return Pair(friendList, sortCriteria)
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

    override fun updateFriend(user: User, friend: Friend) {
        friendJpaRepository.save(FriendJpaEntity.fromFriend(user, friend))
    }
}