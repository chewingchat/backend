package org.chewing.v1.model

import java.time.LocalDateTime

class Phone private constructor(
    val country: String,
    val number: String,
) {
    companion object {
        fun of(country: String, number: String): Phone {
            return Phone(
                country = country,
                number = number,
            )
        }
    }
}