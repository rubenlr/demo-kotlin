package com.rubenlr.demo.controllers

import com.rubenlr.demo.services.AccountService
import com.rubenlr.demo.services.ValidationException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class AccountController(private val accountService: AccountService) {

    @GetMapping("/accounts/{userId}")
    fun listAccounts(@PathVariable("userId") userId: Long, model: Model): String {
        model.addAttribute("userId", userId)
        model.addAttribute("assetsAccount", accountService.getAssetsWithAccounts(userId))
        return "accounts/list"
    }

    @GetMapping("/accounts/create/{userId}/{symbol}")
    fun createAccount(
        @PathVariable("userId") userId: Long,
        @PathVariable("symbol") symbol: String,
        redirectAttributes: RedirectAttributes
    ): String {
        try {
            accountService.save(userId, symbol)
        } catch (e: ValidationException) {
            redirectAttributes.addFlashAttribute("errorMessage", e.message)
        }
        return "redirect:/accounts/$userId"
    }
}