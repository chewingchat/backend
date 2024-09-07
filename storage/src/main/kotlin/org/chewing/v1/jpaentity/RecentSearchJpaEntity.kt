package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.jpaentity.friend.FriendJpaEntity
import org.hibernate.annotations.DynamicInsert
import java.util.*

@DynamicInsert
@Entity
@Table(name = "recent_search", schema = "chewing")
class RecentSearchJpaEntity(
    @Id
    @Column(name = "recent_search_id")
    val recentSearchId: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns(
        value = [
            JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            JoinColumn(name = "friend_id", referencedColumnName = "friend_id")
        ]
    )
    val friend: FriendJpaEntity

) : BaseEntity() {
    companion object {
        fun fromRecentSearch(friend: FriendJpaEntity): RecentSearchJpaEntity {
            return RecentSearchJpaEntity(
                friend = friend
            )
        }
    }
}