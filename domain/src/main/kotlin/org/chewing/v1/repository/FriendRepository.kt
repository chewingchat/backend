package org.chewing.v1.repository

import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.User
import org.springframework.stereotype.Repository

@Repository
interface FriendRepository {

    fun readFriends(userId: User.UserId): List<Friend>
    fun readFriendsByIds(friendIds: List<User.UserId>, userId: User.UserId): List<Friend>
    fun appendFriend(user:User, friendName: User.UserName, targetUser: User)

    fun removeFriend(userId: User.UserId, friendId: User.UserId)

    fun readFriend(userId: User.UserId,friendId: User.UserId): Friend?

    fun checkFriend(userId: User.UserId, friendId: User.UserId): Boolean

    fun updateFriend(user: User, friend: Friend)
    fun updateFavorite(user: User, friendId: User.UserId, favorite: Boolean)
    fun updateName(user: User, friendId: User.UserId, friendName: User.UserName)
    fun deleteAllByUserId(userId: String)
}