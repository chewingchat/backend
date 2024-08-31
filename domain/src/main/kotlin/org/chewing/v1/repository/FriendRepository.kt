package org.chewing.v1.repository

import org.chewing.v1.model.Friend
import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.User
import org.springframework.stereotype.Repository

@Repository
interface FriendRepository {

    fun readFriends(userId: User.UserId): List<Friend>

    fun appendFriend(user:User, friend: Friend)

    fun removeFriend(userId: User.UserId, friendId: User.UserId)

    fun readFriend(userId: User.UserId,friendId: User.UserId): Pair<User, Friend>?

    fun checkFriend(userId: User.UserId, friendId: User.UserId): Boolean

    fun updateFriend(user: User, friend: Friend)
}