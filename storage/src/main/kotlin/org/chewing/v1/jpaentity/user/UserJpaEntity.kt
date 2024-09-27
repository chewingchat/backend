package org.chewing.v1.jpaentity.user

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.jpaentity.emoticon.EmoticonJpaEntity
import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.User
import org.chewing.v1.model.UserContent
import org.chewing.v1.model.media.Media
import org.hibernate.annotations.DynamicInsert
import java.util.*

@DynamicInsert
@Entity
@Table(name = "`user`", schema = "chewing")
internal class UserJpaEntity(
    @Id
    private val userId: String = UUID.randomUUID().toString(),

    private var pictureUrl: String,

    private var backgroundPictureUrl: String,

    private var userFirstName: String,

    private var userLastName: String,

    private var birth: String,
) : BaseEntity() {
    companion object {
        fun fromUser(user: User): UserJpaEntity {
            return UserJpaEntity(
                userId = user.userId,
                userFirstName = user.name.firstName,
                userLastName = user.name.lastName,
                birth = user.birth,
                pictureUrl = user.image.url,
                backgroundPictureUrl = user.backgroundImage.url
            )
        }

        fun generate(userContent: UserContent): UserJpaEntity {
            return UserJpaEntity(
                userFirstName = userContent.name.firstName(),
                userLastName = userContent.name.lastName(),
                birth = userContent.birth,
                pictureUrl = "",
                backgroundPictureUrl = ""
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
        )
    }

    fun updateUserPictureUrl(media: Media) {
        this.pictureUrl = media.url
    }

    fun id(): String {
        return this.userId
    }
}
