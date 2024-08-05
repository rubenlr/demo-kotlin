package com.rubenlr.demo.controllers

import com.rubenlr.demo.services.ExchangeService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.math.BigDecimal

@Controller
@RequestMapping("/exchange")
class ExchangeController(private val exchangeService: ExchangeService) {

    @PostMapping("/{userId}")
    fun performExchange(
        @PathVariable userId: Long,
        @RequestParam fromAccountId: Long,
        @RequestParam toAccountId: Long,
        @RequestParam value: BigDecimal,
        redirectAttributes: RedirectAttributes
    ): String {
        exchangeService.exchange(fromAccountId, toAccountId, value)
        redirectAttributes.addFlashAttribute("message", "Exchange successful")
        return "redirect:/accounts/$userId"
    }
}