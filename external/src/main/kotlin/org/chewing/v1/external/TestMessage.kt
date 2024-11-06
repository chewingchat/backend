package org.chewing.v1.external

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class TestMessage @JsonCreator constructor(
    @JsonProperty("message") val message: String
)
