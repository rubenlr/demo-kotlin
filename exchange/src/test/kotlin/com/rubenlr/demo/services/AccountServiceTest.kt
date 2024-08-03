package com.rubenlr.demo.services

import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.User
import com.rubenlr.demo.repositories.AccountRepository
import com.rubenlr.demo.repositories.AssetRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class AccountServiceTest {

    @MockK
    private lateinit var accountRepository: AccountRepository

    @MockK
    private lateinit var assetRepository: AssetRepository

    private lateinit var accountService: AccountService

    private lateinit var accounts: List<Account>
    private lateinit var assets: List<Asset>
    private lateinit var users: List<User>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        accountService = AccountService(accountRepository, assetRepository)

        users = FakeDataProvider.getUsers(50)
        assets = FakeDataProvider.getAssets(10)
        accounts = FakeDataProvider.getAccounts (users, assets).take(10)
    }

    @Test
    fun `should return all accounts for all users`() {
        every { assetRepository.findAll() } returns assets

        users.forEach { user ->
            every { accountRepository.findByUserId(user.id) } returns accounts.filter { x -> x.user.id == user.id}

            val result = accountService.getAssetsWithAccounts(user.id)
            Assertions.assertEquals(assets.size, result.size)

            assets.forEach { asset ->
                val expectedAccount = accounts.firstOrNull { x -> x.user.id == user.id && x.asset.id == asset.id }
                assertEquals(expectedAccount, result.get(asset))
            }

            verify(exactly = 1) { accountRepository.findByUserId(user.id) }
        }

        verify(exactly = users.size) { assetRepository.findAll() }
    }
}