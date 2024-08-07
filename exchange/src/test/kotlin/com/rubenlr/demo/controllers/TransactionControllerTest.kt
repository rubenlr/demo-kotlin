package com.rubenlr.demo.controllers

import com.ninjasquad.springmockk.MockkBean
import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Transaction
import com.rubenlr.demo.services.AccountService
import com.rubenlr.demo.services.TransactionService
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import kotlin.test.Test

@ExtendWith(SpringExtension::class)
@WebMvcTest(TransactionsController::class)
class TransactionControllerTest {


    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var accountService: AccountService

    @MockkBean
    private lateinit var transactionService: TransactionService

    private lateinit var transactionsController: TransactionsController

    private lateinit var account: Account
    private lateinit var transactions: List<Transaction>

    @BeforeEach
    fun setUp() {
        val users = FakeDataProvider.getUsers()
        val assets = FakeDataProvider.getAssets()
        val accounts = FakeDataProvider.getAccounts(users, assets).shuffled().take(1)
        account = accounts.first()
        transactions = FakeDataProvider.getTransactions(accounts)
    }

    @Test
    fun `should exhibit transaction list`() {
        every { accountService.getById(account.id) } returns Optional.of(account)
        every { transactionService.getAllByIdAccount(account.id) } returns transactions

        mockMvc.perform(get("/transactions/${account.id}"))
            .andExpect(status().isOk)

        verify(exactly = 1) { accountService.getById(account.id)  }
        verify(exactly = 1) { transactionService.getAllByIdAccount(account.id) }
    }
}