package com.tovelop.maphant.exception

import com.tovelop.maphant.controller.BlockController
import com.tovelop.maphant.type.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice(assignableTypes = [BlockController::class])
class BlockExceptionHandler {

    @ExceptionHandler(value = [IllegalStateException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(e: IllegalStateException): ErrorResponse<String> {
        val errorMessage = e.message ?: "알 수 없는 에러 발생"
        return ErrorResponse(errorMessage)
    }
}