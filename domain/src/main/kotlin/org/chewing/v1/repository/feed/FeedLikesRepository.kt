package org.chewing.v1.repository.feed

import org.chewing.v1.model.feed.FeedInfo

interface FeedLikesRepository {
    fun likes(feedId: String, userId: String)
    fun unlikes(feedId: String, userId: String)
    fun unlikeAll(feedIds: List<String>)
    fun checkLike(feedId: String, userId: String): Boolean
}