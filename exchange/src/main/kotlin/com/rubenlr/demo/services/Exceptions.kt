package com.rubenlr.demo.services

open class DemoGlobalException(message: String) : Exception(message)
open class Validation(message: String) : DemoGlobalException(message)
class InvalidInputException(message: String) : Validation(message)
class RecordAlreadyExistsException(message: String) : Validation(message)