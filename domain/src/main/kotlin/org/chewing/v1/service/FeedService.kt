package org.chewing.v1.service

import org.chewing.v1.implementation.feed.*
import org.chewing.v1.implementation.friend.FriendReader
import org.chewing.v1.implementation.media.FileProcessor
import org.chewing.v1.model.User
import org.chewing.v1.model.feed.*
import org.chewing.v1.model.friend.Friend
import org.springframework.stereotype.Service
import java.io.File

@Service
class FeedService(
    private val feedReader: FeedReader,
    private val feedLocker: FeedLocker,
    private val feedRemover: FeedRemover,
    private val feedValidator: FeedValidator,
    private val fileProcessor: FileProcessor,
    private val feedProcessor: FeedProcessor,
    private val feedEnricher: FeedEnricher,
    private val friendReader: FriendReader,
    private val feedFilter: FeedFilter
) {
    fun getFeed(userId: User.UserId, feedId: Feed.FeedId): FulledFeed {
        return feedReader.readFulledFeed(feedId)
    }

    fun getFriendFeedsWithOwner(userId: User.UserId, feedIds: List<Feed.FeedId>, friends: List<Friend>): List<FriendFeed> {
        // 피드와 소유자 ID를 읽어옴
        val feedWithOwnerId = feedReader.readFulledFeedsByOwner(feedIds, friends.map { it.friend.userId })

        // 피드 좋아요 상태를 읽어옴
        val likedFeedIds = feedReader.readFeedsLike(feedWithOwnerId.map { it.first.feed.id }, userId)

        // FeedFilter를 사용하여 유효한 피드와 친구 쌍을 필터링
        val filteredFriendFeeds = feedFilter.filterFriendFeeds(feedWithOwnerId, friends)

        // FriendFeed 객체를 생성
        return feedEnricher.enrichFriendFeed(filteredFriendFeeds, likedFeedIds)
    }
    fun getFriendFeeds(userId: User.UserId, friendId: User.UserId): List<FriendFeed> {
        val friend = friendReader.readFriend(userId, friendId)
        val feeds = feedReader.readFulledFeedsByUserId(friendId)
        val likedFeedIds = feedReader.readFeedsLike(feeds.map { it.feed.id }, userId)
        return feedEnricher.enrichFriendFeeds(friend, feeds, likedFeedIds)
    }

    fun addFeedLikes(userId: User.UserId, feedId: Feed.FeedId, target: FeedTarget) {
        feedValidator.isAlreadyLiked(feedId, userId)
        feedLocker.lockFeedLikes(feedId, userId, target)
    }

    fun deleteFeedLikes(userId: User.UserId, feedId: Feed.FeedId, target: FeedTarget) {
        feedValidator.isAlreadyUnliked(feedId, userId)
        feedLocker.lockFeedUnLikes(feedId, userId, target)
    }

    fun deleteFeeds(userId: User.UserId, feedIds: List<Feed.FeedId>) {
        feedValidator.isFeedsOwner(feedIds, userId)
        val oldMedias = feedRemover.removeFeeds(feedIds)
        fileProcessor.processOldFiles(oldMedias)
    }

    fun createFeed(userId: User.UserId, files: List<File>, topic: String) {
        val medias = fileProcessor.processNewFiles(userId, files)
        feedProcessor.processNewFeed(medias, userId, topic)
    }
}