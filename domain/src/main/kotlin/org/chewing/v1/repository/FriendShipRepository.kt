package org.chewing.v1.repository

import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.user.User
import org.chewing.v1.model.user.UserName
import org.chewing.v1.model.friend.FriendShip
import org.springframework.stereotype.Repository

@Repository
interface FriendShipRepository {

    fun reads(userId: String, accessStatus: AccessStatus): List<FriendShip>
    fun readsByIds(friendIds: List<String>, userId: String, accessStatus: AccessStatus): List<FriendShip>
    fun append(user: User, friendName: UserName, targetUser: User)

    fun remove(userId: String, friendId: String)
    fun block(userId: String, friendId: String)
    fun read(userId: String, friendId: String): FriendShip?
    fun updateFavorite(userId: String, friendId: String, favorite: Boolean)
    fun updateName(userId: String, friendId: String, friendName: UserName)
}