package com.rubenlr.demo.services

import com.rubenlr.demo.data.entities.User
import com.rubenlr.demo.repositories.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        user = User(id = 1, name = "John", email = "john@test.com")
    }

    @Test
    fun `should return all users`() {
        val users = listOf(user)
        `when`(userRepository.findAll()).thenReturn(users)

        val result = userService.getAllUsers()

        assertEquals(users, result)
        verify(userRepository, times(1)).findAll()
    }
}