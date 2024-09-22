package org.chewing.v1.model.emoticon

import org.chewing.v1.model.media.Image
import org.chewing.v1.model.media.Media


class EmoticonPack private constructor(
    val id: String,
    val name: String,
    val media: Media,
    val emoticons: List<Emoticon>
) {
    companion object {
        fun of(
            id: String,
            name: String,
            url: String,
            emoticons: List<Emoticon>
        ): EmoticonPack {
            return EmoticonPack(
                id = id,
                name = name,
                media = Image.of(url, 0),
                emoticons = emoticons
            )
        }
    }
}