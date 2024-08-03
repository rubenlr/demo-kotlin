package com.rubenlr.demo.services

import com.rubenlr.demo.data.entities.User
import com.rubenlr.demo.repositories.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class UserServiceTest {

    @MockK
    private lateinit var userRepository: UserRepository

    private lateinit var userService: UserService

    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        userService = UserService(userRepository)
        user = User(id = 1, name = "John", email = "john@test.com")
    }

    @Test
    fun `should return all users`() {
        val users = listOf(user)
        every { userRepository.findAll() } returns users

        val result = userService.getAllUsers()

        assertEquals(users, result)

        io.mockk.verify(exactly = 1) { userRepository.findAll() }
    }
}