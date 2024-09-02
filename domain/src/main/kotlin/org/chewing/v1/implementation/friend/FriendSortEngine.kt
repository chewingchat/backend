package org.chewing.v1.implementation.friend

import org.chewing.v1.model.Friend
import org.chewing.v1.model.FriendSearch
import org.chewing.v1.model.SortCriteria

object FriendSortEngine {

    fun sortFriends(friends: List<Friend>, sortCriteria: SortCriteria): List<Friend> {
        return friends.sortedWith(getFriendComparator(sortCriteria))
    }

    fun sortFriendSearchHistory(friends: List<FriendSearch>, sortCriteria: SortCriteria): List<FriendSearch> {
        return friends.sortedWith(getFriendSearchHistoryComparator(sortCriteria))
    }

    private fun getFriendComparator(sortCriteria: SortCriteria): Comparator<Friend> {
        return when (sortCriteria) {
            SortCriteria.NAME -> Comparator.comparing<Friend?, String?> { it.friend.name.firstName() }.thenComparing(
                Comparator.comparing { it.friend.name.lastName() }
            )

            SortCriteria.FAVORITE -> Comparator.comparing<Friend?, Int?> {
                if (it.isFavorite) 0 else 1
            }.thenComparing(
                Comparator.comparing<Friend?, String?> { it.friend.name.firstName() }.thenComparing(
                    Comparator.comparing { it.friend.name.lastName() }
                )
            )

            else -> throw IllegalArgumentException("존재하지 않는 정렬 기준: $sortCriteria")
        }
    }

    private fun getFriendSearchHistoryComparator(sortCriteria: SortCriteria): Comparator<FriendSearch> {
        return when (sortCriteria) {
            SortCriteria.DATE -> Comparator.comparing { it.searchTime }
            else -> throw IllegalArgumentException("존재하지 않는 정렬 기준: $sortCriteria")
        }
    }
}