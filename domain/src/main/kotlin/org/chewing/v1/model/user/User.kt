package org.chewing.v1.model.user

import org.chewing.v1.model.AccessStatus
import org.chewing.v1.model.media.Image


class User private constructor(
    val userId: String,
    val name: UserName,
    val birth: String,
    val image: Image,
    val backgroundImage: Image,
    val type: AccessStatus
) {
    companion object {
        fun of(
            userId: String,
            firstName: String,
            lastName: String,
            birth: String,
            image: Image,
            backgroundImage: Image,
            type: AccessStatus
        ): User {
            return User(
                userId = userId,
                birth = birth,
                image = image,
                backgroundImage = backgroundImage,
                name = UserName.of(firstName, lastName),
                type = type
            )
        }
    }
}
