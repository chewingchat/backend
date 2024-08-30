package org.chewing.v1.util

import org.chewing.v1.response.ErrorResponse
import org.chewing.v1.response.HttpResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import mu.KotlinLogging
import org.chewing.v1.error.AuthorizationException
import org.chewing.v1.error.ConflictException
import org.chewing.v1.error.NotFoundException


@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(AuthorizationException::class)
    protected fun handleAuthorizationException(e: AuthorizationException): ErrorResponseEntity {
        val errorCode = e.errorCode
        logger.info { "인증 예외 발생: ${errorCode.message}" }
        return ResponseHelper.error(HttpStatus.UNAUTHORIZED,ErrorResponse.from(errorCode))    }

    @ExceptionHandler(NotFoundException::class)
    protected fun handleNotFoundException(e: NotFoundException): ErrorResponseEntity {
        val errorCode = e.errorCode
        logger.info { "리소스를 찾을 수 없음: ${errorCode.message}" }
        return ResponseHelper.error(HttpStatus.NOT_FOUND,ErrorResponse.from(errorCode))
    }

    @ExceptionHandler(ConflictException::class)
    protected fun handleConflictException(e: ConflictException): ErrorResponseEntity {
        val errorCode = e.errorCode
        logger.info { "충돌 예외 발생: ${errorCode.message}" }
        return ResponseHelper.error(HttpStatus.CONFLICT,ErrorResponse.from(errorCode))
    }
}
