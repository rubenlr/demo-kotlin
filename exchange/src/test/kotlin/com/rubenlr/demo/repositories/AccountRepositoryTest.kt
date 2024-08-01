package com.rubenlr.demo.repositories

import com.rubenlr.demo.data.entities.Account
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.jvm.optionals.getOrNull

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AccountRepositoryTest : RepositoryBaseTest() {

    @Autowired
    private lateinit var accountRepository: AccountRepository

    fun assertAccount(expected: Account, actual: Account?) {
        assertNotNull(actual, "account not found")
        assertEquals(expected.id, actual?.id)
        assertEquals(expected.asset.symbol, actual?.asset?.symbol)
        assertEquals(expected.user.email, actual?.user?.email)
        assertEquals(expected.balance, actual?.balance)
    }

    @Test
    fun `should save and find account`() {
        val savedUser = getSavedUsers().shuffled().first()
        val savedAsset = getSavedAssets().shuffled().first()

        val savedAccount = accountRepository.saveAndFlush(
            Account(user = savedUser, asset = savedAsset, balance = randomDecimal(max = 1000.0))
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
        val savedUsers = getSavedUsers()
        val savedAsset = getSavedAssets()

        val accounts = savedUsers.flatMap { user ->
            savedAsset.map { asset ->
                Account(user = user, asset = asset, balance = randomDecimal(max = 1000.0))
            }
        }
        val savedAccounts = accountRepository.saveAllAndFlush(accounts)

        savedUsers.forEach { user ->
            validateAccounts(savedAccounts, user.id)
        }
    }
}