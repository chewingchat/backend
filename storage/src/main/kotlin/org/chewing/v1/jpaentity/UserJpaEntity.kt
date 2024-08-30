package org.chewing.v1.jpaentity

import jakarta.persistence.*
import org.chewing.v1.common.BaseEntity
import org.chewing.v1.model.Image
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

    @Column(name = "status_message", nullable = false)
    val statusMessage: String,

    @Column(name = "user_name", nullable = false)
    val userName: String,

    @Column(name = "birthday", nullable = false)
    val birth: String

) : BaseEntity() {
    companion object {
        fun fromUser(user: User): UserJpaEntity {
            return UserJpaEntity(
                user.userId.value(),
                user.image.value(),
                user.statusMessage,
                user.name,
                user.birth,
            )
        }
    }

    fun toUser(): User {
        return User.withId(
            this.id,
            this.userName,
            this.birth,
            this.statusMessage,
            Image.of(this.pictureUrl)
        )
    }
}
