package org.chewing.v1.dto.response.emoticon

import org.chewing.v1.model.emoticon.Emoticon
import org.chewing.v1.model.emoticon.EmoticonPack

data class EmoticonPacksResponse(
    val emoticonPacks: List<EmoticonPackResponse>
) {
    companion object {
        fun of(emoticonPacks: List<EmoticonPack>): EmoticonPacksResponse {
            return EmoticonPacksResponse(
                emoticonPacks = emoticonPacks.map { EmoticonPackResponse.of(it) }
            )
        }
    }

    data class EmoticonPackResponse(
        val id: String,
        val emoticons: List<EmoticonResponse>
    ) {
        companion object {
            fun of(emoticonPack: EmoticonPack): EmoticonPackResponse {
                return EmoticonPackResponse(
                    id = emoticonPack.id,
                    emoticons = emoticonPack.emoticons.map { EmoticonResponse.of(it) }
                )
            }
        }

        data class EmoticonResponse(
            val id: String,
            val name: String,
            val imageUrl: String
        ) {
            companion object {
                fun of(emoticon: Emoticon): EmoticonResponse {
                    return EmoticonResponse(
                        id = emoticon.id,
                        name = emoticon.name,
                        imageUrl = emoticon.media.url
                    )
                }
            }
        }
    }
}