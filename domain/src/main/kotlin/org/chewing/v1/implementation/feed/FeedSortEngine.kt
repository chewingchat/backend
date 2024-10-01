package org.chewing.v1.implementation.feed

import org.chewing.v1.model.SortCriteria
import org.chewing.v1.model.feed.Feed
import org.chewing.v1.model.feed.FeedInfo
import org.chewing.v1.model.feed.FriendFeed
import java.time.LocalDateTime

object FeedSortEngine {
    fun sort(feedInfos: List<Feed>, sortCriteria: SortCriteria): List<Feed> {
        return feedInfos.sortedWith(getFeedComparator(sortCriteria))
    }

    private fun getFeedComparator(sortCriteria: SortCriteria): Comparator<Feed> {
        return when (sortCriteria) {
            SortCriteria.DATE -> Comparator.comparing<Feed?, LocalDateTime?> { it.feed.uploadAt }
            else -> throw IllegalArgumentException("존재하지 않는 정렬 기준: $sortCriteria")
        }
    }

}