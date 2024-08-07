package com.rubenlr.demo.services

import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.ExchangeRateProvider
import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Transaction
import com.rubenlr.demo.data.entities.TransactionType
import com.rubenlr.demo.repositories.AccountRepository
import com.rubenlr.demo.repositories.TransactionRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
class ExchangeServiceTest {
    @MockK
    private lateinit var accountRepository: AccountRepository

    @MockK
    private lateinit var transactionRepository: TransactionRepository

    @MockK
    private lateinit var exchangeRateProvider: ExchangeRateProvider

    private lateinit var exchangeService: ExchangeService

    private lateinit var value: BigDecimal
    private lateinit var from: Account
    private lateinit var transactions: List<Transaction>
    private lateinit var to: Account

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        exchangeService = ExchangeService(accountRepository, transactionRepository, exchangeRateProvider)

        val users = FakeDataProvider.getUsers(1)
        val assets = FakeDataProvider.getAssets(7)
        val accounts = FakeDataProvider.getAccounts(users, assets).shuffled()
        transactions = FakeDataProvider.getTransactions(accounts)

        from = accounts.first()
        to = accounts.first { it.id != from.id }
        value = BigDecimal.valueOf(Random.nextDouble(1.0, 100.0))
    }

    @Test
    fun `should validate from account`() {
        every { accountRepository.findById(from.id) } returns Optional.empty()
        every { accountRepository.findById(to.id) } returns Optional.of(to)

        assertThrows<InvalidInputException> {
            exchangeService.exchange(from.id, to.id, value)
        }
    }

    @Test
    fun `should validate to account`() {
        every { accountRepository.findById(from.id) } returns Optional.of(from)
        every { accountRepository.findById(to.id) } returns Optional.empty()

        assertThrows<InvalidInputException> {
            exchangeService.exchange(from.id, to.id, value)
        }
    }

    @Test
    fun `should not allow transactions higher than current balance`() {
        from.balance = value.subtract(BigDecimal.valueOf(0.1))
        every { accountRepository.findById(from.id) } returns Optional.of(from)
        every { accountRepository.findById(to.id) } returns Optional.of(to)

        assertThrows<BalanceInsufficientException> {
            exchangeService.exchange(from.id, to.id, value)
        }
    }

    @Test
    fun `should save accounts and transaction with update balance`() {
        from.balance = value.add(BigDecimal.valueOf(100.0))
        to.balance = BigDecimal.valueOf(0.0)
        val rate = BigDecimal.valueOf(Random.nextDouble(1.5, 5.0))

        val fromAfterExchange = from.copy(balance = from.balance - value)
        val toAfterExchange = to.copy(balance = value.multiply(rate))
        val listAccounts = listOf(fromAfterExchange, toAfterExchange)

        every { accountRepository.findById(from.id) } returns Optional.of(from)
        every { accountRepository.findById(to.id) } returns Optional.of(to)
        every { exchangeRateProvider.getRate(from.asset.symbol, to.asset.symbol) } returns rate
        every { accountRepository.saveAll(listAccounts) } returns listAccounts
        every { transactionRepository.save(any<Transaction>()) } returns mockk()

        exchangeService.exchange(from.id, to.id, value)

        verify(exactly = 1) {
            transactionRepository.save(match { transaction ->
                transaction.fromAccount == from &&
                        transaction.fromValue == value &&
                        transaction.type == TransactionType.EXCHANGE &&
                        transaction.toAccount == to &&
                        transaction.toValue == value.multiply(rate)
            })
        }
    }
}