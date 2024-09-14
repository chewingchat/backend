package org.chewing.v1.jpaentity.user

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.jpaentity.emoticon.EmoticonJpaEntity
import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.User
import org.hibernate.annotations.DynamicInsert
import java.util.*

@DynamicInsert
@Entity
@Table(name = "`user`", schema = "chewing")
class UserJpaEntity(
    @Id
    val userId: String = UUID.randomUUID().toString(),

    val pictureUrl: String,

    val backgroundPictureUrl: String,

    val userFirstName: String,

    val userLastName: String,

    val birth: String,
) : BaseEntity() {
    companion object {
        fun fromUser(user: User): UserJpaEntity {
            return UserJpaEntity(
                user.userId.value(),
                user.image.url,
                user.backgroundImage.url,
                user.status.statusMessage,
                user.name.firstName(),
                user.name.lastName()
            )
        }
    }

    fun toUser(): User {
        return User.of(
            this.userId,
            this.userFirstName,
            this.userLastName,
            this.birth,
            Image.of(this.pictureUrl, 0),
            Image.of(this.backgroundPictureUrl, 0),
            "",
            Emoticon.empty(),
            ""
        )
    }
}
