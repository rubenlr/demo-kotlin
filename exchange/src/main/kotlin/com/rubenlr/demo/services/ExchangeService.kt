package com.rubenlr.demo.services

import com.rubenlr.demo.data.ExchangeRateProvider
import com.rubenlr.demo.data.entities.Transaction
import com.rubenlr.demo.data.entities.TransactionType
import com.rubenlr.demo.repositories.AccountRepository
import com.rubenlr.demo.repositories.TransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.OffsetDateTime

@Service
class ExchangeService(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val exchangeRateProvider: ExchangeRateProvider
){
    @Transactional
    fun exchange(fromAccountId: Long, toAccountId: Long, value: BigDecimal) {
        val from = accountRepository.findById(fromAccountId)
            .orElseThrow { throw InvalidInputException("[from] Account $fromAccountId not found") }
        val to = accountRepository.findById(toAccountId)
            .orElseThrow { throw InvalidInputException("[to] Account $toAccountId not found") }

        if (value > from.balance) {
            throw BalanceInsufficientException("Origin account doesn't have enough balance to take. Available: ${from.balance}. Requested: $value")
        }

        val rate = exchangeRateProvider.getRate(from.asset.symbol, to.asset.symbol)
        val convertedValue = value.multiply(rate)
        val transaction = Transaction(
            fromAccount = from,
            fromValue = value,
            type = TransactionType.EXCHANGE,
            toAccount = to,
            toValue = convertedValue,
            executedAt = OffsetDateTime.now()
        )

        from.balance = from.balance.subtract(value)
        to.balance = to.balance.add(convertedValue)

        accountRepository.saveAll(listOf(from, to))
        transactionRepository.save(transaction)
    }
}