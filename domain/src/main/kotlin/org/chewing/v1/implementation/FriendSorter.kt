package org.chewing.v1.implementation

import org.chewing.v1.model.Friend
import org.chewing.v1.model.SortCriteria

object FriendSorter {

    fun sortFriendCards(friends: List<Friend>, sortCriteria: SortCriteria): List<Friend> {
        return friends.sortedWith(getComparator(sortCriteria))
    }

    private fun getComparator(sortCriteria: SortCriteria): Comparator<Friend> {
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
}