package com.rubenlr.demo.controllers

import com.rubenlr.demo.services.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UserController(private val userService: UserService) {

    @GetMapping("/users")
    fun listUsers(model: Model): String {
        model.addAttribute("users", userService.getAllUsers())
        return "userList"
    }
}
