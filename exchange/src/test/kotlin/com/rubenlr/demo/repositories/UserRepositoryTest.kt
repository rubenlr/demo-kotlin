package com.rubenlr.demo.repositories

import com.rubenlr.demo.data.entities.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Testcontainers

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest : RepositoryBaseTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should save and find user by id`() {
        val user = User(name = "John", email = "john@test.com")

        val savedUser = userRepository.save(user)
        val foundUser = userRepository.findById(savedUser.id).orElse(null)

        assertNotNull(foundUser)
        assertEquals(savedUser.id, foundUser?.id)
        assertEquals(savedUser.name, foundUser?.name)
        assertEquals(savedUser.email, foundUser?.email)
    }

    @Test
    fun `should find all users`() {
        val user1 = User(name = "John", email = "john@test.com")
        val user2 = User(name = "Jane", email = "jane@test.com")
        userRepository.save(user1)
        userRepository.save(user2)

        val users = userRepository.findAll()

        assertEquals(2, users.size)
    }
}