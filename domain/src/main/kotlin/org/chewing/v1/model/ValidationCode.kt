package org.chewing.v1.model

import java.time.LocalDateTime
import java.util.*

class ValidationCode(
    val code: String,
    val expiredAt: LocalDateTime?
) {
    companion object {
        fun of(code: String, expiredAt: LocalDateTime): ValidationCode {
            return ValidationCode(
                code = code,
                expiredAt = expiredAt
            )
        }

        fun empty(): ValidationCode {
            return ValidationCode(
                code = "",
                expiredAt = null
            )
        }

        fun onlyWithCode(code: String): ValidationCode {
            return ValidationCode(
                code = code,
                expiredAt = null
            )
        }

        fun generate(): ValidationCode {
            return ValidationCode(
                code = UUID.randomUUID().toString(),
                expiredAt = LocalDateTime.now().plusMinutes(5)
            )
        }
    }

    fun validateCode(code: String): Boolean {
        return this.code == code
    }

    fun validateExpired(): Boolean {
        return expiredAt?.isBefore(LocalDateTime.now()) ?: true
    }
}