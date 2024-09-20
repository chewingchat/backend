package org.chewing.v1.repository

import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.User
import org.springframework.stereotype.Repository

@Repository
interface FriendRepository {

    fun readFriendsWithStatus(userId: User.UserId): List<Friend>

    fun appendFriend(user:User, friend: Friend)

    fun removeFriend(userId: User.UserId, friendId: User.UserId)

    fun readFriend(userId: User.UserId,friendId: User.UserId): Friend?

    fun checkFriend(userId: User.UserId, friendId: User.UserId): Boolean

    fun updateFriend(user: User, friend: Friend)

    fun readFriendWithStatus(userId: User.UserId, friendId: User.UserId): Friend?

    // 츄가
    fun deleteAllByUserId(userId: String)
}