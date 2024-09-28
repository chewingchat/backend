package org.chewing.v1.model.media


interface Media {
    val url: String
    val type: MediaType
    val index: Int
    val isBasic: Boolean
}
