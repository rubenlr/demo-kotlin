package com.rubenlr.demo.services

import com.rubenlr.demo.data.entities.User
import com.rubenlr.demo.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }
}