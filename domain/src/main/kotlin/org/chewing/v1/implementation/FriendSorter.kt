package org.chewing.v1.implementation

import org.chewing.v1.model.Friend
import org.chewing.v1.model.SortCriteria

object FriendSorter {

    fun sortFriends(friends: List<Friend>, sortCriteria: SortCriteria): List<Friend> {
        return friends.sortedWith(getComparator(sortCriteria))
    }

    private fun getComparator(sortCriteria: SortCriteria): Comparator<Friend> {
        return when (sortCriteria) {
            SortCriteria.NAME_ASC -> Comparator.comparing { it.friend.name }
            SortCriteria.FAVORITE_ASC -> Comparator.comparing<Friend?, Int?> {
                if (it.favorite) 0 else 1
            }.thenComparing(
                Comparator.comparing { it.friend.name }
            )

            else -> throw IllegalArgumentException("존재하지 않는 정렬 기준: $sortCriteria")
        }
    }
}