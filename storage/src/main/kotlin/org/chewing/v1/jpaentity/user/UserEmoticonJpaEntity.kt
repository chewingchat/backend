package org.chewing.v1.jpaentity.user

import jakarta.persistence.*
import org.chewing.v1.jpaentity.emoticon.EmoticonPackJpaEntity
import org.chewing.v1.model.emoticon.EmoticonPack
import org.chewing.v1.model.User
@Entity
@Table(name = "user_emoticon", schema = "chewing")
class UserEmoticonJpaEntity(
    @EmbeddedId
    val id: UserEmoticonId,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    val user: UserJpaEntity,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("emoticonPackId")
    @JoinColumn(name = "emoticon_pack_id", insertable = false, updatable = false)
    val emoticonPack: EmoticonPackJpaEntity
) {
    companion object {
        fun fromUserEmoticon(user: User, emoticonPack: EmoticonPack): UserEmoticonJpaEntity {
            return UserEmoticonJpaEntity(
                id = UserEmoticonId(userId = user.userId.value(), emoticonPackId = emoticonPack.id),
                user = UserJpaEntity.fromUser(user),
                emoticonPack = EmoticonPackJpaEntity.fromEmoticonPack(emoticonPack)
            )
        }
    }
}