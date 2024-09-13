package org.chewing.v1.jpaentity

import jakarta.persistence.*;
import org.chewing.v1.jpaentity.common.BaseEntity
import org.chewing.v1.jpaentity.emoticon.EmoticonJpaEntity
import org.chewing.v1.model.Email
import org.chewing.v1.model.Phone
import org.chewing.v1.model.User
import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.media.Image
import org.hibernate.annotations.DynamicInsert
import java.util.*

@Entity
@DynamicInsert
@Table(name = "email", schema = "chewing")
class EmailJpaEntity(
    @Id
    @Column(name = "email_id")
    val emailId: String = UUID.randomUUID().toString(),

    @Column(name = "email")
    val emailAddress: String, // email 수정

    @Column(name = "first_authorized")
    val firstAuthorized: Boolean,
) : BaseEntity() {
    companion object {
        fun fromEmail(email: Email): EmailJpaEntity{
            return EmailJpaEntity(
                emailId = email.emailId,
                emailAddress = email.emailAddress,
                firstAuthorized = email.isAuthorizedFirst,
            )
        }
    }

}
