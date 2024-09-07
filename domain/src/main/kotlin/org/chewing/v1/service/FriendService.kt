package org.chewing.v1.service

import org.chewing.v1.implementation.*
import org.chewing.v1.implementation.feed.FeedChecker
import org.chewing.v1.implementation.feed.FeedReader
import org.chewing.v1.implementation.friend.*
import org.chewing.v1.model.*
import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.friend.FriendFeed
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
    private val feedReader: FeedReader,
    private val feedChecker: FeedChecker,
) {
    fun addFriendWithEmail(userId: User.UserId, friendName: User.UserName, email: String) {
        val friend = Friend.generate(friendReader.readFriendWithEmail(email), friendName)
        friendChecker.isAlreadyFriend(userId, friend.friend.userId)
        val user = userReader.readUser(userId)
        friendAppender.appendFriend(user, friend)
    }

    fun addFriendWithPhone(userId: User.UserId, friendName: User.UserName, phone: String) {
        val friend = Friend.generate(friendReader.readFriendWithPhoneNumber(phone), friendName)
        friendChecker.isAlreadyFriend(userId, friend.friend.userId)
        val user = userReader.readUser(userId)
        friendAppender.appendFriend(user, friend)
    }

    @Transactional
    fun removeFriend(userId: User.UserId, friendId: User.UserId) {
        friendRemover.removeFriend(userId, friendId)
    }

    fun getFriends(userId: User.UserId, sort: SortCriteria): Pair<User, List<Friend>> {
        val friends = friendReader.readFriendsWithStatus(userId)
        val user = userReader.readUserWithStatus(userId)
        return Pair(user, FriendSortEngine.sortFriends(friends, sort))
    }


    @Transactional
    fun changeFriendFavorite(userId: User.UserId, friendId: User.UserId, favorite: Boolean) {
        val user = userReader.readUser(userId)
        val friend = friendReader.readFriend(userId, friendId)
        val updateFriend = friend.updateFavorite(favorite)
        friendUpdater.updateFriend(user, updateFriend)
    }

    @Transactional
    fun changeFriendName(userId: User.UserId, friendId: User.UserId, friendName: User.UserName) {
        val user = userReader.readUser(userId)
        val friend = friendReader.readFriend(userId, friendId)
        val updateFriend = friend.updateName(friendName)
        friendUpdater.updateFriend(user, updateFriend)
    }

    fun getFriendDetail(userId: User.UserId, friendId: User.UserId): Pair<Friend, List<FriendFeed>> {
        // 친구 정보를 읽어옵니다.
        val friend = friendReader.readFriendWithStatus(userId, friendId)

        // 친구의 피드를 읽어옵니다.
        val feeds = feedReader.readFeedsWithDetails(friendId)

        // 피드에 대한 좋아요 상태를 확인합니다.
        val likedFeedIds = feedChecker.checkFeedsLike(feeds.map { it.feedId }, userId)

        // 피드 좋아요 상태를 추가합니다.
        val friendsFeed = feeds.map { feed ->
            val isLiked = likedFeedIds[feed.feedId] ?: false
            FriendFeed.of(feed, isLiked)
        }

        // 결과를 반환합니다.
        return Pair(friend, friendsFeed)
    }
}