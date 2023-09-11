package com.tovelop.maphant.exception

import com.tovelop.maphant.controller.ProfileController
import com.tovelop.maphant.type.response.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [ProfileController::class])
class ProfileExceptionHandler {
    @ExceptionHandler(value = [IllegalStateException::class])
    fun handleException(e: RuntimeException): ErrorResponse<String> {
        val errorResponse = e.message ?: "알 수 없는 에러 발생"
        return ErrorResponse(errorResponse)
    }
}