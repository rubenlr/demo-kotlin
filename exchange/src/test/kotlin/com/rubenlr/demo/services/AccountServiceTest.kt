package com.rubenlr.demo.services

import com.rubenlr.demo.Helper
import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.AssetType
import com.rubenlr.demo.data.entities.User
import com.rubenlr.demo.repositories.AccountRepository
import com.rubenlr.demo.repositories.AssetRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
class AccountServiceTest {

    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var assetRepository: AssetRepository

    @InjectMocks
    private lateinit var accountService: AccountService

    private lateinit var accounts: List<Account>
    private lateinit var assets: List<Asset>
    private lateinit var users: List<User>

    @BeforeEach
    fun setUp() {
        users = (1L..10).map { number ->
            User(number, "john", "jhon@test.com")
        }

        assets = listOf(
            Asset(1, "EURO", AssetType.FIAT),
            Asset(2, "BTC", AssetType.CRYPTO),
            Asset(3, "ETH", AssetType.CRYPTO),
            Asset(4, "Apple", AssetType.STOCK),
            Asset(5, "Tesla", AssetType.STOCK),
            Asset(6, "GOLD", AssetType.COMMODITIES),
            Asset(7, "SILVER", AssetType.COMMODITIES),
        )

        val localAssets = assets.toMutableList()

        accounts = (1L..7).map { number ->
            Account(
                number,
                users.shuffled().first(),
                localAssets.removeAt(localAssets.size - 1),
                Helper.randomDecimal(max = 1000.0)
            )
        }
    }

    @Test
    fun `should return all accounts for all users`() {
        Mockito.`when`(assetRepository.findAll()).thenReturn(assets)

        users.forEach { user ->
            Mockito.`when`(accountRepository.findByUserId(user.id)).thenReturn(accounts)

            val result = accountService.getAssetsWithAccounts(user.id)

            Assertions.assertEquals(assets.size, result.size)

            assets.forEach { asset ->
                val expectedAccount = accounts.firstOrNull() { x -> x.user.id == user.id && x.asset.id == asset.id }
                val actualAccount = result.entries.firstOrNull { x -> x.key.id == asset.id }

                // All assets must have an asset key, even if there is no account associated.
                assertNotNull(actualAccount)

                if (expectedAccount != null)
                {
                    assertEquals(expectedAccount.balance, actualAccount.value?.balance)
                    assertEquals(expectedAccount.user.email, actualAccount.value?.user?.email)
                    assertEquals(expectedAccount.asset.symbol, actualAccount.value?.asset?.symbol)
                }
            }

            Mockito.verify(accountRepository, Mockito.times(1)).findByUserId(user.id)
        }

        Mockito.verify(assetRepository, Mockito.times(users.size)).findAll()
    }
}