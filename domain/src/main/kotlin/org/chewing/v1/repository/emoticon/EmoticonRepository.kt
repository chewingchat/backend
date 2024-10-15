package org.chewing.v1.repository.emoticon

import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.emoticon.EmoticonPack

interface EmoticonRepository {
    fun readEmoticon(emoticonId: String): Emoticon?
    fun readEmoticons(emoticonIds: List<String>): List<Emoticon>
    fun readEmoticonPacks(emoticonPackIds: List<String>): List<EmoticonPack>
}