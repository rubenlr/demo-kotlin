package com.rubenlr.demo.controllers

import com.ninjasquad.springmockk.MockkBean
import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.User
import com.rubenlr.demo.services.AccountService
import com.rubenlr.demo.services.UserService
import com.rubenlr.demo.services.ValidationException
import io.mockk.every
import io.mockk.verify
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.test.assertNotNull

@ExtendWith(SpringExtension::class)
@WebMvcTest(AccountController::class)
class AccountControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var userService: UserService

    @MockkBean
    private lateinit var accountService: AccountService

    private lateinit var users: List<User>
    private lateinit var assets: List<Asset>
    private lateinit var accounts: List<Account>

    @BeforeEach
    fun setUp() {
        users = FakeDataProvider.getUsers()
        assets = FakeDataProvider.getAssets()
        accounts = FakeDataProvider.getAccounts(users, assets)
    }

    @Test
    fun `should return OK status`() {
        val assetsWithAccounts = assets.associateWith { asset ->
            accounts.firstOrNull() { it.user.id == users.first().id && it.asset.id == asset.id }
        }
        val userId = users.first().id
        every { accountService.getAssetsWithAccounts(userId) } returns assetsWithAccounts

        val resultActions = mockMvc.perform(get("/accounts/${userId}")).andExpect(status().isOk)

        assets.forEach { asset ->
            resultActions.andExpect(content().string(containsString(asset.symbol)))
        }

        verify(exactly = 1) { accountService.getAssetsWithAccounts(userId) }
    }

    @Test
    fun `should redirect to account list on successful account creation`() {
        val userId = users.first().id
        val symbol = assets.first().symbol
        val account = accounts.firstOrNull { it.user.id == userId && it.asset.symbol == symbol}

        assertNotNull(account)

        every { accountService.save(userId, symbol) } returns account

        mockMvc.perform(get("/accounts/create/$userId/$symbol"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/accounts/$userId"))

        verify(exactly = 1) { accountService.save(userId, symbol) }
    }

    @Test
    fun `should add error message and redirect on ValidationException`() {
        val userId = users.first().id
        val symbol = assets.first().symbol
        val account = accounts.firstOrNull { it.user.id == userId && it.asset.symbol == symbol}

        assertNotNull(account)
        val errorMessage = "Validation error occurred"

        every { accountService.save(userId, symbol) } throws ValidationException(errorMessage)

        mockMvc.perform(get("/accounts/create/$userId/$symbol"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/accounts/$userId"))
            .andExpect(flash().attribute("errorMessage", errorMessage))

        verify(exactly = 1) { accountService.save(userId, symbol) }
    }
}