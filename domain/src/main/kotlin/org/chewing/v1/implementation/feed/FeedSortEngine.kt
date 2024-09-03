package org.chewing.v1.implementation.feed

import org.chewing.v1.model.feed.FeedDetail
import org.chewing.v1.model.SortCriteria

object FeedSortEngine {

    fun sortFeedDetails(feedDetails: List<FeedDetail>, sortCriteria: SortCriteria): List<FeedDetail> {
        return feedDetails.sortedWith(getFeedComparator(sortCriteria))
    }


    private fun getFeedComparator(sortCriteria: SortCriteria): Comparator<FeedDetail> {
        return when (sortCriteria) {
            SortCriteria.INDEX -> Comparator.comparing { it.index }
            else -> throw IllegalArgumentException("존재하지 않는 정렬 기준: $sortCriteria")
        }
    }
}