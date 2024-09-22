package org.chewing.v1.repository

import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.User
import org.chewing.v1.model.emoticon.EmoticonPack

interface EmoticonRepository {
    fun readEmoticonPacks(userId: String): List<EmoticonPack>
    fun readEmoticon(emoticonId: String): Emoticon
    fun readEmoticons(emoticonIds: List<String>): List<Emoticon>
}