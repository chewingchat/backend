package org.chewing.v1.service

import org.chewing.v1.implementation.*
import org.chewing.v1.model.Friend
import org.chewing.v1.model.SortCriteria
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
    private val friendChecker: FriendChecker
) {
    fun addFriendWithEmail(userId: User.UserId, friendName: User.UserName, email: String) {
        val friend = Friend.generate(friendReader.readFriendWithEmail(email), friendName)
        friendChecker.isAlreadyFriend(userId, friend.friend.userId)
        val user = userReader.readUserById(userId)
        friendAppender.appendFriend(user, friend)
    }
    fun addFriendWithPhone(userId: User.UserId, friendName: User.UserName, phone: String) {
        val friend = Friend.generate(friendReader.readFriendWithPhoneNumber(phone), friendName)
        friendChecker.isAlreadyFriend(userId, friend.friend.userId)
        val user = userReader.readUserById(userId)
        friendAppender.appendFriend(user, friend)
    }

    @Transactional
    fun removeFriend(userId: User.UserId, friendId: User.UserId) {
        friendRemover.removeFriend(userId, friendId)
    }

    fun getFriends(userId: User.UserId, sort: SortCriteria): Pair<User, List<Friend>> {
        val friends = friendReader.readFriends(userId)
        val user = userReader.readUserById(userId)
        return Pair(user, FriendSorter.sortFriendCards(friends, sort))
    }


    @Transactional
    fun changeFriendFavorite(userId: User.UserId, friendId: User.UserId, favorite: Boolean) {
        val (user, friend) = friendReader.readFriend(userId, friendId)
        val updateFriend = friend.updateFavorite(favorite)
        friendUpdater.updateFriend(user, updateFriend)
    }

    @Transactional
    fun changeFriendName(userId: User.UserId, friendId: User.UserId, friendName: User.UserName) {
        val (user, friend) = friendReader.readFriend(userId, friendId)
        val updateFriend = friend.updateName(friendName)
        friendUpdater.updateFriend(user, updateFriend)
    }
}