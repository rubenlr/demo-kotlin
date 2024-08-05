package com.rubenlr.demo.services

import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Transaction
import com.rubenlr.demo.repositories.TransactionRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TransactionServiceTest {

    @MockK
    private lateinit var transactionRepository: TransactionRepository

    private lateinit var transactionService: TransactionService


    private lateinit var accounts: List<Account>

    private lateinit var transactions: List<Transaction>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        transactionService = TransactionService(transactionRepository)
        val gen = FakeDataProvider.getTransactions(2, 5)
        accounts = gen.accounts
        transactions = gen.transactions
    }

    @Test
    fun `should return all assets`() {
        accounts.forEach { account ->
            val accountId = account.id
            val expectedTransactions = transactions.filter { it.fromAccount.id == accountId || it.toAccount.id == accountId }

            every { transactionRepository.findAllByAccountId(accountId) } returns expectedTransactions

            val actualTransactions = transactionService.getAllByIdAccount(accountId)
            assertEquals(expectedTransactions, actualTransactions)

            verify(exactly = 1) { transactionRepository.findAllByAccountId(accountId) }
        }
    }
}