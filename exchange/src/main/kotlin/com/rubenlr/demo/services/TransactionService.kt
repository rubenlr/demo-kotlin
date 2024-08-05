package com.rubenlr.demo.services

import com.rubenlr.demo.repositories.TransactionRepository
import org.springframework.stereotype.Service

@Service
class TransactionService(private val transactionRepository: TransactionRepository) {

    fun getAllByIdAccount(accountId: Long) = transactionRepository.findAllByAccountId(accountId)

    fun save(fromAccountId: Long, fromValue: Double, toAccountId: Long) {

    }
}