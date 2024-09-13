package org.chewing.v1.error

class UnauthorizedException(errorCode: ErrorCode) : RuntimeException() {
    val code = errorCode.code
    val errorMessage = errorCode.message
}
