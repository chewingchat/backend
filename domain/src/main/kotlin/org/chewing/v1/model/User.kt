package org.chewing.v1.model

import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.media.Image
import org.chewing.v1.model.media.Media


class User private constructor(
    val userId: String,
    val name: UserName,
    val birth: String,
    val image: Image,
    val backgroundImage: Image
) {
    companion object {
        fun of(
            userId: String,
            firstName: String,
            lastName: String,
            birth: String,
            image: Image,
            backgroundImage: Image,
        ): User {
            return User(
                userId = userId,
                birth = birth,
                image = image,
                backgroundImage = backgroundImage,
                name = UserName.of(firstName, lastName)
            )
        }
    }
}
