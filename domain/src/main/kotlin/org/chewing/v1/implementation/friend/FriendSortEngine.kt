package org.chewing.v1.implementation.friend

import org.chewing.v1.model.friend.Friend
import org.chewing.v1.model.friend.UserSearch
import org.chewing.v1.model.SortCriteria

object FriendSortEngine {

    fun sort(friends: List<Friend>, sortCriteria: SortCriteria): List<Friend> {
        return friends.sortedWith(getFriendComparator(sortCriteria))
    }

    fun sortFriendSearchedHistory(friends: List<UserSearch>, sortCriteria: SortCriteria): List<UserSearch> {
        return friends.sortedWith(getFriendSearchHistoryComparator(sortCriteria))
    }

    private fun getFriendComparator(sortCriteria: SortCriteria): Comparator<Friend> {
        return when (sortCriteria) {
            SortCriteria.NAME -> Comparator.comparing<Friend?, String?> { it.user.name.lastName() }.thenComparing(
                Comparator.comparing { it.user.name.firstName() }
            )

            SortCriteria.FAVORITE -> Comparator.comparing<Friend?, Int?> {
                if (it.isFavorite) 0 else 1
            }.thenComparing(
                Comparator.comparing<Friend?, String?> { it.user.name.lastName() }.thenComparing(
                    Comparator.comparing { it.user.name.firstName() }
                )
            )

            else -> throw IllegalArgumentException("존재하지 않는 정렬 기준: $sortCriteria")
        }
    }

    private fun getFriendSearchHistoryComparator(sortCriteria: SortCriteria): Comparator<UserSearch> {
        return when (sortCriteria) {
            SortCriteria.DATE -> Comparator.comparing { it.searchAt }
            else -> throw IllegalArgumentException("존재하지 않는 정렬 기준: $sortCriteria")
        }
    }
}