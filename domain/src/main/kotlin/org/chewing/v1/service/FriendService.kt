package org.chewing.v1.service

import org.chewing.v1.implementation.*
import org.chewing.v1.model.Friend
import org.chewing.v1.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FriendService(
    private val friendReader: FriendReader,
    private val friendRemover: FriendRemover,
    private val friendAppender: FriendAppender,
    private val friendUpdater: FriendUpdater,
    private val userReader: UserReader,
) {
    @Transactional
    fun addFriend(userId: User.UserId, friendId: User.UserId, friendName: String) {
        val friend = Friend.generate(userReader.readUserById(friendId), friendName)
        val user = userReader.readUserById(userId)
        friendAppender.appendFriend(user, friend)
    }

    @Transactional
    fun removeFriend(userId: User.UserId, friendId: User.UserId) {
        friendRemover.removeFriend(userId, friendId)
    }

    fun getFriends(userId: User.UserId): List<Friend> {
        val (users, sortCriteria) = friendReader.readFriends(userId)
        return FriendSorter.sortFriends(users, sortCriteria)
    }

    @Transactional
    fun setFavorite(userId: User.UserId, friendId: User.UserId, favorite: Boolean) {
        val (user, friend) = friendReader.readFriend(userId, friendId)
        val updateFriend = friend.updateFavorite(favorite)
        friendUpdater.updateFriend(user, updateFriend)
    }
}