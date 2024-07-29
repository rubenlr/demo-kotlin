package com.rubenlr.demo

import com.rubenlr.demo.data.entities.User
import com.rubenlr.demo.repositories.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataSeeder {

    @Bean
    fun initialize(userRepository: UserRepository) = ApplicationRunner {
        if (userRepository.count() == 0L) {
            val users = listOf(
                User(name = "John Doe", email = "john.doe@example.com"),
                User(name = "Jane Doe", email = "jane.doe@example.com")
            )
            userRepository.saveAll(users)
        }
    }
}