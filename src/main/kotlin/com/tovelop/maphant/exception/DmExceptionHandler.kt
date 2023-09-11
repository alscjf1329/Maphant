package com.tovelop.maphant.exception

import com.tovelop.maphant.controller.DmController
import com.tovelop.maphant.controller.RoomController
import com.tovelop.maphant.type.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice(assignableTypes = [RoomController::class, DmController::class])
class DmExceptionHandler {
    @ExceptionHandler(value = [NullPointerException::class,IllegalStateException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleException(e: RuntimeException): ErrorResponse<String> {
        val errorMessage = e.message ?: "알 수 없는 에러 발생"
        return ErrorResponse(errorMessage)
    }
}