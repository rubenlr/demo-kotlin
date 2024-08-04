package com.rubenlr.demo.repositories

import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import kotlin.random.Random
import kotlin.test.assertNotNull

@RepositoryTest
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
            val generatedAccounts = accounts.filter { it.user.id == user.id }

            assertEquals(generatedAccounts, actualSavedAccount)
        }
    }


    @Test
    fun `should not find accounts by user if user doesn't exists`() {
        val avalilableUserId = (1L..500).find { index -> users.none { it.id == index } }

        assertNotNull(avalilableUserId)
        assertEquals(emptyList<User>(), accountRepository.findByUserId(avalilableUserId))
    }

    @Test
    fun `should find accounts by userId and assetId`() {
        accounts.forEach { account ->
            val actualAccount = accountRepository.findByUserIdAndAssetId(account.user.id, account.asset.id)
            assertEquals(Optional.of(account), actualAccount)
        }
    }

    @Test
    fun `should not find accounts by userId and assetId if any userId doesn't match`() {
        val availableUserId = (1L..500).find { n ->
            accounts.none { it.user.id == n } && users.any { it.id == n }
        }
        val assetId = (1L..500).find { index -> users.any { it.id == index } }

        assertNotNull(assetId)
        assertNotNull(availableUserId)

        assertEquals(Optional.empty<Account>(), accountRepository.findByUserIdAndAssetId(availableUserId, assetId))
    }

    @Test
    fun `should not find accounts by userId and assetId if assetId doesn't match`() {
        val userId = (1L..500).find { index -> users.any { it.id == index } }
        val availableAssetId = (1L..500).find { n ->
            accounts.none { it.asset.id == n } && assets.any { it.id == n }
        }

        assertNotNull(userId)
        assertNotNull(availableAssetId)

        assertEquals(Optional.empty<Account>(), accountRepository.findByUserIdAndAssetId(userId, availableAssetId))
    }
}