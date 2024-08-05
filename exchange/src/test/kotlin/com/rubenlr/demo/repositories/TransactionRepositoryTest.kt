package com.rubenlr.demo.repositories

import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.Transaction
import com.rubenlr.demo.data.entities.User
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
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
    @Transactional
    fun setUp() {
        users = userRepository.saveAllAndFlush(FakeDataProvider.getUsers(2))
        assets = assetRepository.saveAllAndFlush(FakeDataProvider.getAssets(3))
        accounts = accountRepository.saveAllAndFlush(FakeDataProvider.getAccounts(users, assets).shuffled().take(3))
        transactions = transactionRepository.saveAllAndFlush(FakeDataProvider.getTransactions(accounts).shuffled().take(3))
    }

    @Test
    fun `should save and find transactions`() {
        val savedTransactions = transactionRepository.findAll()
        assertEquals(transactions, savedTransactions)
    }

    @Test
    fun `should get transactions by userAccount`() {
        accounts.forEach { account ->
            val expectedTransaction = transactions.filter {
                it.fromAccount.id == account.id || it.toAccount.id == account.id
            }
            if (expectedTransaction.isNotEmpty()) {
                val actualTransactions = transactionRepository.findAllByAccountId(account.id)
                assertEquals(expectedTransaction, actualTransactions)
            }
        }
    }
}