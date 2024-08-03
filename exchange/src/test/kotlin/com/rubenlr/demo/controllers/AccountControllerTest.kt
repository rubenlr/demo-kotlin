package com.rubenlr.demo.controllers

import com.ninjasquad.springmockk.MockkBean
import com.rubenlr.demo.data.entities.User
import com.rubenlr.demo.services.AccountService
import io.mockk.every
import io.mockk.verify
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(UserController::class)
class AccountControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var accountService: AccountService

//    @Test
//    fun `should return OK status`() {
//        val users = listOf(User(1, "John", "john@test.com"), User(2, "Jane", "jane@test.com"))
//        every { userService.getAllUsers() } returns users
//
//        mockMvc.perform(get("/users"))
//            .andExpect(status().isOk)
//            .andExpect(content().string(containsString(users[0].name)))
//            .andExpect(content().string(containsString(users[0].email)))
//            .andExpect(content().string(containsString(users[1].name)))
//            .andExpect(content().string(containsString(users[1].email)))
//
//        verify(exactly = 1) { userService.getAllUsers() }
//    }

}