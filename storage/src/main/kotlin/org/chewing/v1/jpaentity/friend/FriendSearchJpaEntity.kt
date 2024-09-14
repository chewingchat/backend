package org.chewing.v1.jpaentity.friend

import jakarta.persistence.*
import org.chewing.v1.jpaentity.user.UserJpaEntity
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.model.friend.FriendSearch
import org.chewing.v1.model.User
import org.hibernate.annotations.DynamicInsert
import java.util.UUID

@Entity
@DynamicInsert
@Table(name = "friend_search", schema = "chewing")
class FriendSearchJpaEntity(
    @Id
    val searchId: String = UUID.randomUUID().toString(),

    val searchText: String,

    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    val user: UserJpaEntity,
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
                user = UserJpaEntity.fromUser(user)
            )
        }
    }
}