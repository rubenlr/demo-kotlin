package com.rubenlr.demo

import com.rubenlr.demo.services.*
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BalanceInsufficientException::class)
    fun handleBalanceInsufficientException(ex: BalanceInsufficientException, model: Model): String {
        model.addAttribute("error", ex.message)
        return "error"
    }

    @ExceptionHandler(RecordAlreadyExistsException::class)
    fun handleRecordAlreadyExistsException(ex: RecordAlreadyExistsException, model: Model): String {
        model.addAttribute("error", ex.message)
        return "error"
    }

    @ExceptionHandler(InvalidInputException::class)
    fun handleInvalidInputException(ex: InvalidInputException, model: Model): String {
        model.addAttribute("error", ex.message)
        return "error"
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(ex: ValidationException, model: Model): String {
        model.addAttribute("error", ex.message)
        return "error"
    }

    @ExceptionHandler(DemoGlobalException::class)
    fun handleDemoGlobalException(ex: DemoGlobalException, model: Model): String {
        model.addAttribute("error", ex.message)
        return "error"
    }
}
