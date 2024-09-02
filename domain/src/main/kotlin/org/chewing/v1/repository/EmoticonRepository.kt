package org.chewing.v1.repository

import org.chewing.v1.model.Emoticon
import org.chewing.v1.model.User

interface EmoticonRepository {
    fun readUserStatusEmoticon(userId: User.UserId): Emoticon
}