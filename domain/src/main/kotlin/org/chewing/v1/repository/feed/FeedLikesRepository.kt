package org.chewing.v1.repository.feed

interface FeedLikesRepository {
    suspend fun likes(feedId: String, userId: String)
    suspend fun unlikes(feedId: String, userId: String)
    fun unlikeAll(feedIds: List<String>)
    fun checkLike(feedId: String, userId: String): Boolean
}
