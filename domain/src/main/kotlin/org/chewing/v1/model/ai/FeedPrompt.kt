package org.chewing.v1.model.ai

class FeedPrompt private constructor(
    val startHint: String,
    val title: String,
    val uploadTime: String,
    val detailsUrl: List<String>,
)
