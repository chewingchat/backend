package org.chewing.v1.repository

import org.chewing.v1.model.feed.FeedInfo

interface FeedLikesRepository {
    fun isAlreadyLiked(feedId: String, userId: String): Boolean
    fun likes(feedId: String, userId: String)
    fun unlikes(feedId: String, userId: String)
    fun unlikeAll(feedIds: List<String>)
    fun checkLike(feedId: String, userId: String): Boolean
}