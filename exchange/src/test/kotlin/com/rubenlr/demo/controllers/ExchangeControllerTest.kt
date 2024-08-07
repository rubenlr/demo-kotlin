package com.rubenlr.demo.controllers

import com.ninjasquad.springmockk.MockkBean
import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.ExchangeResultDto
import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.services.ExchangeService
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import kotlin.random.Random
import kotlin.test.Test

@ExtendWith(SpringExtension::class)
@WebMvcTest(ExchangeController::class)
class ExchangeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var exchangeService: ExchangeService

    private lateinit var accounts: List<Account>

    @BeforeEach
    fun setUp() {
        val users = FakeDataProvider.getUsers(1)
        val assets = FakeDataProvider.getAssets(5)
        accounts = FakeDataProvider.getAccounts(users, assets).shuffled().filter { it.user.id == users.first().id }
    }

    @Test
    fun `should redirect to accounts after exchange`() {
        val from = accounts.first()
        val to = accounts.first { it.id != from.id }
        val value = BigDecimal.valueOf(Random.nextDouble())
        val userId = from.user.id

        every { exchangeService.exchange(from.id, to.id, value) } returns ExchangeResultDto("Success")

        val requestPost = post("/exchange/$userId")
            .param("fromAccountId", from.id.toString())
            .param("toAccountId", to.id.toString())
            .param("value", value.toString())

        mockMvc.perform(requestPost)
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/accounts/$userId"))

        verify(exactly = 1) { exchangeService.exchange(from.id, to.id, value) }
    }
}