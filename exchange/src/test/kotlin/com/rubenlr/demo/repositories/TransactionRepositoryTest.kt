package com.rubenlr.demo.repositories

import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.Transaction
import com.rubenlr.demo.data.entities.User
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.Test
import kotlin.test.assertEquals


@RepositoryTest
class TransactionRepositoryTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var assetRepository: AssetRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var transactionRepository: TransactionRepository

    private lateinit var users: List<User>
    private lateinit var assets: List<Asset>
    private lateinit var accounts: List<Account>
    private lateinit var transactions: List<Transaction>

    @BeforeEach
    fun setUp() {
        val gen = FakeDataProvider.getTransactions(2, 5)
        users = userRepository.saveAllAndFlush(gen.users)
        assets = assetRepository.saveAllAndFlush(gen.assets)
        accounts = accountRepository.saveAllAndFlush(gen.accounts)
        transactions = transactionRepository.saveAllAndFlush(gen.transactions)
    }

    @Test
    fun `should save and find transactions`() {
        val savedTransactions = transactionRepository.findAll()

        assertEquals(transactions, savedTransactions)
    }
}