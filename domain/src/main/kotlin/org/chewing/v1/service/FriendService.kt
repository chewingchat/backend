package org.chewing.v1.service

import org.chewing.v1.implementation.friend.*
import org.chewing.v1.implementation.user.UserReader
import org.chewing.v1.model.*
import org.chewing.v1.model.contact.Contact
import org.chewing.v1.model.friend.Friend
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FriendService(
    private val friendReader: FriendReader,
    private val friendRemover: FriendRemover,
    private val friendAppender: FriendAppender,
    private val friendUpdater: FriendUpdater,
    private val userReader: UserReader,
    private val friendChecker: FriendChecker,
    private val friendFinder: FriendFinder,
) {
    fun addFriend(
        userId: User.UserId,
        friendName: User.UserName,
        contact: Contact
    ) {
        val targetUser = userReader.readUserByContact(contact)
        friendChecker.isAlreadyFriend(userId, targetUser.userId)
        val user = userReader.readUser(userId)
        friendAppender.appendFriend(user, friendName, targetUser)
    }

    fun removeFriend(userId: User.UserId, friendId: User.UserId) {
        friendRemover.removeFriend(userId, friendId)
    }

    fun getFriends(userId: User.UserId, sort: SortCriteria): List<Friend> {
        val friends = friendFinder.findFriendsWithStatus(userId)
        return FriendSortEngine.sortFriends(friends, sort)
    }

    fun getOnlyFriends(userId: User.UserId): List<Friend> {
        return friendReader.readFriends(userId)
    }

    @Transactional
    fun changeFriendFavorite(userId: User.UserId, friendId: User.UserId, favorite: Boolean) {
        val user = userReader.readUser(userId)
        friendChecker.isFriend(userId, friendId)
        friendUpdater.updateFavorite(user, friendId, favorite)
    }

    @Transactional
    fun changeFriendName(userId: User.UserId, friendId: User.UserId, friendName: User.UserName) {
        val user = userReader.readUser(userId)
        friendChecker.isFriend(userId, friendId)
        friendUpdater.updateName(user, friendId, friendName)
    }
}