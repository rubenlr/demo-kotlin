package com.rubenlr.demo.repositories

import MyPostgresConfiguration
import com.rubenlr.demo.FakeDataProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Testcontainers

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ContextConfiguration(classes = [MyPostgresConfiguration::class])
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should save and find user by id`() {
        val users = FakeDataProvider.getUsers(10)
        userRepository.saveAllAndFlush(users)

        assertEquals(users, userRepository.findAll())
    }
}