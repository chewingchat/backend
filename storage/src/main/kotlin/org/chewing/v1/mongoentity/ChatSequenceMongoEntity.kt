package org.chewing.v1.mongoentity

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ChatSequenceMongoEntity(
    @Id
    val id: String? = null,
    val roomId: String,
    val userId: String,
    var seqNumber: Long
)