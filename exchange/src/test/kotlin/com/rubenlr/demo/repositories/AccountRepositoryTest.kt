package com.rubenlr.demo.repositories

import MyPostgresConfiguration
import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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
import java.math.BigDecimal
import kotlin.jvm.optionals.getOrNull
import kotlin.random.Random

@ExtendWith(SpringExtension::class)
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    fun assertAccount(expected: Account, actual: Account?) {
        assertNotNull(actual, "account not found")
        assertEquals(expected.id, actual?.id)
        assertEquals(expected.asset.symbol, actual?.asset?.symbol)
        assertEquals(expected.user.email, actual?.user?.email)
        assertEquals(expected.balance, actual?.balance)
    }

    fun getUsers(): List<User> {
        val users = FakeDataProvider.getUsers(Random.nextLong(10, 20))
        userRepository.saveAllAndFlush(users)
        return userRepository.findAll()
    }

    fun getAssets(): List<Asset> {
        val assets = FakeDataProvider.getAssets(Random.nextLong(10, 20))
        assetRepository.saveAllAndFlush(assets)
        return assetRepository.findAll()
    }

    @Test
    fun `should save and find account`() {
        // Prep
        val savedUser = getUsers().first()
        val savedAsset = getAssets().first()

        val savedAccount = accountRepository.saveAndFlush(
            Account(user = savedUser, asset = savedAsset, balance = BigDecimal.valueOf(Random.nextLong(1, 1000)))
        )
        val foundAccount = accountRepository.findById(savedAccount.id).getOrNull()

        assertAccount(savedAccount, foundAccount)
    }

    private fun validateAccounts(savedAccounts: List<Account>, userId: Long) {

        val savedAccountsForThisUser = savedAccounts.filter { ac -> ac.user.id == userId }
        val foundAccountsByUser = accountRepository.findByUserId(userId)

        assertEquals(savedAccountsForThisUser.size, foundAccountsByUser.size)

        savedAccountsForThisUser.forEach { expectedAccount ->
            assertAccount(expectedAccount, foundAccountsByUser.firstOrNull { x -> x.id == expectedAccount.id })
        }
    }

    @Test
    fun `should find accounts by user`() {
        val savedUsers = getUsers()
        val savedAsset = getAssets()

        val accounts = savedUsers.flatMap { user ->
            savedAsset.map { asset ->
                Account(user = user, asset = asset, balance = BigDecimal.valueOf(Random.nextLong(1, 1000)))
            }
        }
        val savedAccounts = accountRepository.saveAllAndFlush(accounts)

        savedUsers.forEach { user ->
            validateAccounts(savedAccounts, user.id)
        }
    }
}