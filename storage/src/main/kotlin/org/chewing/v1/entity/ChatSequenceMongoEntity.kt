package org.chewing.v1.entity

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ChatSequenceMongoEntity(
    @Id val roomId: String,
    var seq: Long
)