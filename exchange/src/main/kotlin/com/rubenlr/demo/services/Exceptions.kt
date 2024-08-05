package com.rubenlr.demo.services

open class DemoGlobalException(message: String) : Exception(message)
open class ValidationException(message: String) : DemoGlobalException(message)
class BalanceInsufficientException(message: String) : ValidationException(message)
class InvalidInputException(message: String) : ValidationException(message)
class RecordAlreadyExistsException(message: String) : ValidationException(message)