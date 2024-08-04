package com.rubenlr.demo.repositories

import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.entities.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@RepositoryTest
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var users: List<User>

    @BeforeEach
    fun setUp() {
        val generatedUsers = FakeDataProvider.getUsers(10)
        users = userRepository.saveAllAndFlush(generatedUsers)
    }

    @Test
    fun `should save and find user by id`() {
        val savedUsers = userRepository.findAll()
        assertEquals(users, savedUsers)
    }
}