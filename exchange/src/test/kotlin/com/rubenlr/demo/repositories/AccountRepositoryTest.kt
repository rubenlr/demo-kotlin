package com.rubenlr.demo.repositories

import MyPostgresConfiguration
import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.random.Random

@ExtendWith(SpringExtension::class)
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ContextConfiguration(classes = [MyPostgresConfiguration::class])
class AccountRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var assetRepository: AssetRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    private lateinit var users: List<User>
    private lateinit var assets: List<Asset>
    private lateinit var accounts: List<Account>

    @BeforeEach
    fun setUp() {
        users = userRepository.saveAllAndFlush(FakeDataProvider.getUsers(Random.nextLong(5, 7)))
        assets = assetRepository.saveAllAndFlush(FakeDataProvider.getAssets(Random.nextLong(5, 7)))
        val savedAccounts = FakeDataProvider.getAccounts(users, assets).take(5)
        accounts = accountRepository.saveAllAndFlush(savedAccounts)
    }

    @Test
    fun `should save and find account`() {
        val savedAccounts = accountRepository.findAll()
        assertEquals(accounts, savedAccounts)
    }

    @Test
    fun `should find accounts by user`() {
        users.forEach { user ->
            val actualSavedAccount = accountRepository.findByUserId(user.id)
            val generatedAccounts = accounts.filter { x -> x.user.id == user.id }

            assertEquals(generatedAccounts, actualSavedAccount, actualSavedAccount.toString())
        }
    }
}