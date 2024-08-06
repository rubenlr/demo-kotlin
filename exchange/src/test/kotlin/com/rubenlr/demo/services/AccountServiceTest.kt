package com.rubenlr.demo.services

import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.entities.*
import com.rubenlr.demo.repositories.AccountRepository
import com.rubenlr.demo.repositories.AssetRepository
import com.rubenlr.demo.repositories.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class AccountServiceTest {

    @MockK
    private lateinit var accountRepository: AccountRepository

    @MockK
    private lateinit var assetRepository: AssetRepository

    @MockK
    private lateinit var userRepository: UserRepository

    private lateinit var accountService: AccountService

    private lateinit var accounts: List<Account>
    private lateinit var assets: List<Asset>
    private lateinit var users: List<User>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        accountService = AccountService(accountRepository, assetRepository, userRepository)

        users = FakeDataProvider.getUsers(50)
        assets = FakeDataProvider.getAssets(10)
        accounts = FakeDataProvider.getAccounts(users, assets).take(10)
    }

    @Test
    fun `should return all accounts for all users`() {
        every { assetRepository.findAll() } returns assets

        users.forEach { user ->
            every { accountRepository.findByUserId(user.id) } returns accounts.filter { it.user.id == user.id }

            val result = accountService.getAssetsWithAccounts(user.id)
            Assertions.assertEquals(assets.size, result.size)

            assets.forEach { asset ->
                val expectedAccount = accounts.firstOrNull { it.user.id == user.id && it.asset.id == asset.id }
                assertEquals(expectedAccount, result.get(asset))
            }

            verify(exactly = 1) { accountRepository.findByUserId(user.id) }
        }

        verify(exactly = users.size) { assetRepository.findAll() }
    }

    @Test
    fun `should save new account`() {
        val user = User(id = 1L, name = "Test User", email = "Test")
        val asset = Asset(id = 1L, symbol = Symbol.BTC, type = AssetType.CRYPTO)
        val account = Account(user = user, asset = asset, balance = BigDecimal.ZERO)

        every { userRepository.findById(account.user.id) } returns Optional.of(account.user)
        every { assetRepository.findBySymbol(account.asset.symbol) } returns Optional.of(account.asset)
        every { accountRepository.saveAndFlush(account) } returns account
        every { accountRepository.findByUserIdAndAssetId(user.id, asset.id) } returns Optional.empty<Account>()

        val savedAccount = accountService.save(account.user.id, account.asset.symbol)
        account.id = savedAccount.id

        assertEquals(account, savedAccount)

        verify(exactly = 1) { userRepository.findById(account.user.id) }
        verify(exactly = 1) { assetRepository.findBySymbol(account.asset.symbol) }
        verify(exactly = 1) { accountRepository.saveAndFlush(account) }
        verify(exactly = 1) { accountRepository.findByUserIdAndAssetId(user.id, asset.id) }
    }

    private fun newId() = kotlin.random.Random.nextLong(50, 1000)

    @Test
    fun `should not save new account if it already exists`() {
        val user = User(id = newId(), name = "Test User", email = "Test")
        val asset = Asset(id = newId(), symbol = Symbol.BTC, type = AssetType.CRYPTO)
        val account = Account(user = user, asset = asset, balance = BigDecimal.ZERO)

        every { userRepository.findById(account.user.id) } returns Optional.of(account.user)
        every { assetRepository.findBySymbol(account.asset.symbol) } returns Optional.of(account.asset)
        every { accountRepository.saveAndFlush(account) } returns account
        every { accountRepository.findByUserIdAndAssetId(user.id, asset.id) } returns Optional.of(account)

        assertThrows<RecordAlreadyExistsException> {
            accountService.save(user.id, asset.symbol)
        }

        verify(exactly = 1) { userRepository.findById(account.user.id) }
        verify(exactly = 1) { assetRepository.findBySymbol(account.asset.symbol) }
        verify(exactly = 0) { accountRepository.saveAndFlush(account) }
        verify(exactly = 1) { accountRepository.findByUserIdAndAssetId(user.id, asset.id) }
    }

    @Test
    fun `should not save new account if user doesn't exist`() {
        val user = User(id = newId(), name = "Test User", email = "Test")
        val asset = Asset(id = newId(), symbol = Symbol.BTC, type = AssetType.CRYPTO)
        val account = Account(user = user, asset = asset, balance = BigDecimal.ZERO)

        every { userRepository.findById(account.user.id) } returns Optional.empty() // no user
        every { assetRepository.findBySymbol(account.asset.symbol) } returns Optional.of(account.asset)
        every { accountRepository.saveAndFlush(account) } returns account
        every { accountRepository.findByUserIdAndAssetId(user.id, asset.id) } returns Optional.of(account)

        val exception = assertThrows<InvalidInputException> {
            accountService.save(user.id, asset.symbol)
        }

        assertNotNull(exception.message)
        exception.message?.let {
            assertTrue(it.contains("User"))
            assertTrue(it.contains(user.id.toString()))
        }

        verify(exactly = 1) { userRepository.findById(account.user.id) }
        verify(exactly = 0) { accountRepository.saveAndFlush(account) }
        verify(exactly = 0) { accountRepository.findByUserIdAndAssetId(user.id, asset.id) }
    }

    @Test
    fun `should not save new account if asset doesn't exist`() {
        val user = User(id = newId(), name = "Test User", email = "Test")
        val asset = Asset(id = newId(), symbol = Symbol.BTC, type = AssetType.CRYPTO)
        val account = Account(user = user, asset = asset, balance = BigDecimal.ZERO)

        every { userRepository.findById(account.user.id) } returns Optional.of(account.user)
        every { assetRepository.findBySymbol(account.asset.symbol) } returns Optional.empty() // no asset
        every { accountRepository.saveAndFlush(account) } returns account
        every { accountRepository.findByUserIdAndAssetId(user.id, asset.id) } returns Optional.of(account)

        val exception = assertThrows<InvalidInputException> {
            accountService.save(user.id, asset.symbol)
        }

        assertNotNull(exception.message)
        exception.message?.let {
            assertTrue(it.contains("Asset"))
            assertTrue(it.contains(asset.symbol.name))
        }

        verify(exactly = 1) { assetRepository.findBySymbol(account.asset.symbol) }
        verify(exactly = 0) { accountRepository.saveAndFlush(account) }
        verify(exactly = 0) { accountRepository.findByUserIdAndAssetId(user.id, asset.id) }
    }
}