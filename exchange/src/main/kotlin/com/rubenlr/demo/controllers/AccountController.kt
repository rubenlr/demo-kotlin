package com.rubenlr.demo.controllers

import com.rubenlr.demo.services.AccountService
import com.rubenlr.demo.services.AssetService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class AccountController(private val accountService: AccountService, private val assetService: AssetService) {

    @GetMapping("/accounts/:idUser")
    fun listAccounts(@PathVariable userId: Long, model: Model): String {
        model.addAttribute("userId", userId)
        model.addAttribute("assetsAccount", accountService.getAssetsWithAccounts(userId))
        return "accounts/list"
    }
}