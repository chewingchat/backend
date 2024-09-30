package org.chewing.v1.model.media


interface Media {
    val category: FileCategory
    val url: String
    val type: MediaType
    val index: Int
    val isBasic: Boolean
}
