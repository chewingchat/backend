package org.chewing.v1.jpaentity.user

import jakarta.persistence.*
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.jpaentity.emoticon.EmoticonJpaEntity
import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.User
import org.hibernate.annotations.DynamicInsert

@DynamicInsert
@Entity
@Table(name = "`user`", schema = "chewing")
class UserJpaEntity(
    @Id
    @Column(name = "user_id")
    val id: String,

    @Column(name = "picture_url", nullable = false)
    val pictureUrl: String,

    @Column(name = "background_picture_url", nullable = false)
    val backgroundPictureUrl: String,

    @Column(name = "status_message", nullable = false)
    val statusMessage: String,

    @Column(name = "user_first_name", nullable = false)
    val userFirstName: String,

    @Column(name = "user_last_name", nullable = false)
    val userLastName: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "emoticon_id", nullable = true)
    val statusEmoticon: EmoticonJpaEntity?,

    @Column(name = "birthday", nullable = false)
    val birth: String

) : BaseEntity() {
    companion object {
        fun fromUser(user: User): UserJpaEntity {
            return UserJpaEntity(
                user.userId.value(),
                user.image.url,
                user.backgroundImage.url,
                user.status.statusMessage,
                user.name.firstName(),
                user.name.lastName(),
                null,
                user.birth
            )
        }
    }

    fun toUser(): User {
        return User.withId(
            this.id,
            this.userFirstName,
            this.userLastName,
            this.birth,
            Image.of(this.pictureUrl),
            Image.of(this.backgroundPictureUrl),
            Emoticon.empty(),
            this.statusMessage
        )
    }

    fun toUserWithStatus(): User {
        val emoticon = this.statusEmoticon?.toEmoticon() ?: Emoticon.empty()
        return User.withId(
            this.id,
            this.userFirstName,
            this.userLastName,
            this.birth,
            Image.of(this.pictureUrl),
            Image.of(this.backgroundPictureUrl),
            emoticon,
            this.statusMessage
        )
    }
}
