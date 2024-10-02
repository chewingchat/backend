package org.chewing.v1.jpaentity.friend

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.friend.UserSearch
import org.chewing.v1.model.user.User
import org.hibernate.annotations.DynamicInsert
import java.util.UUID

@Entity
@DynamicInsert
@Table(name = "friend_search", schema = "chewing")
internal class UserSearchJpaEntity(
    @Id
    val searchId: String = UUID.randomUUID().toString(),
    val searchText: String,
    val userId: String,
): BaseEntity() {
    fun toFriendSearch(): UserSearch {
        return UserSearch.of(
            keyword = searchText,
            searchTime = createdAt!!.toLocalDate()
        )
    }

    companion object {
        fun fromFriendSearch(user: User, userSearch: UserSearch): UserSearchJpaEntity {
            return UserSearchJpaEntity(
                searchText = userSearch.keyword,
                userId = user.userId
            )
        }
    }
}