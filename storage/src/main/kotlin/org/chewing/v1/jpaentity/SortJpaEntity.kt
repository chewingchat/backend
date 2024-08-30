package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.model.SortCriteria
import java.util.*

@Entity
@Table(name = "sort", schema = "chewing")
class SortJpaEntity(
    @Id
    @Column(name = "sort_id")
    val sortId: String = UUID.randomUUID().toString(),

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_room_sort", nullable = false)
    val chatRoomSort: SortCriteria,

    @Enumerated(EnumType.STRING)
    @Column(name = "friends_sort", nullable = false)
    val friendSort: SortCriteria,

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserJpaEntity,
)