package org.chewing.v1.repository

import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.friend.FriendInfo
import org.springframework.stereotype.Repository

@Repository
interface FriendRepository {

    fun readFriends(userId: String): List<FriendInfo>
    fun readFriendsByIds(friendIds: List<String>, userId: String): List<FriendInfo>
    fun appendFriend(user: User, friendName: UserName, targetUser: User)

    fun removeFriend(userId: String, friendId: String)
    fun blockFriend(userId: String, friendId: String)

    fun readFriend(userId: String,friendId: String): FriendInfo?
    fun checkFriend(userId: String, friendId: String): Boolean
    fun readFriendShip(userId: String, friendId: String): Pair<FriendInfo,FriendInfo>?
    fun updateFavorite(user: User, friendId: String, favorite: Boolean)
    fun updateName(user: User, friendId: String, friendName: UserName)
}