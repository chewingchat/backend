package org.chewing.v1.jpaentity.user

import jakarta.persistence.*
import org.chewing.v1.model.emoticon.EmoticonPack
import org.chewing.v1.model.user.User
@Entity
@Table(name = "user_emoticon", schema = "chewing")
internal class UserEmoticonJpaEntity(
    @EmbeddedId
    private val id: UserEmoticonId,
) {
    companion object {
        fun fromUserEmoticon(user: User, emoticonPack: EmoticonPack): UserEmoticonJpaEntity {
            return UserEmoticonJpaEntity(
                id = UserEmoticonId(userId = user.userId, emoticonPackId = emoticonPack.id),
            )
        }
    }
}