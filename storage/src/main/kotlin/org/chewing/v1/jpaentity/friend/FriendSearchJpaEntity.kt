package org.chewing.v1.jpaentity.friend

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.user.User
import org.hibernate.annotations.DynamicInsert
import java.util.UUID

@Entity
@DynamicInsert
@Table(name = "friend_search", schema = "chewing")
internal class FriendSearchJpaEntity(
    @Id
    val searchId: String = UUID.randomUUID().toString(),
    val searchText: String,
    val userId: String,
): BaseEntity() {
    fun toFriendSearch(): FriendSearch {
        return FriendSearch.of(
            keyword = searchText,
            searchTime = createdAt!!.toLocalDate()
        )
    }

    companion object {
        fun fromFriendSearch(user: User, friendSearch: FriendSearch): FriendSearchJpaEntity {
            return FriendSearchJpaEntity(
                searchText = friendSearch.keyword,
                userId = user.userId
            )
        }
    }
}