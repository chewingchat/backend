package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.common.BaseEntity
import org.chewing.v1.model.FriendSearch
import org.chewing.v1.model.User
import org.hibernate.annotations.DynamicInsert
import java.util.UUID

@Entity
@DynamicInsert
@Table(name = "friend_search", schema = "chewing")
class FriendSearchJpaEntity(
    @Id
    @Column(name = "search_id")
    val searchId: String = UUID.randomUUID().toString(),


    @Column(name = "search_text", nullable = false)
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