package org.chewing.v1.repository

import org.chewing.v1.model.feed.FeedInfo

interface FeedLikesRepository {
    fun isAlreadyLiked(feedId: String, userId: String): Boolean
    fun likes(feedInfo: FeedInfo, userId: String)
    fun unlikes(feedInfo: FeedInfo, userId: String)
    fun checkLike(feedId: String, userId: String): Boolean
}