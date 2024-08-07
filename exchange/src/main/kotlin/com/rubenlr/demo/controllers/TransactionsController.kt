package com.rubenlr.demo.controllers

import com.rubenlr.demo.data.entities.Transaction
import com.rubenlr.demo.services.AccountService
import com.rubenlr.demo.services.TransactionService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.util.stream.Collectors

@Controller
@RequestMapping("/transactions")
class TransactionsController(
    private val accountService: AccountService,
    private val transactionService: TransactionService
) {

    @GetMapping("/{accountId}")
    fun list(@PathVariable accountId: Long, model: Model): String {
        val account = accountService.getById(accountId).orElse(null)
        val transactions = transactionService.getAllByIdAccount(accountId).stream()
            .sorted(Comparator.comparing(Transaction::executedAt).reversed())
            .collect(Collectors.toList())
        model.addAttribute("account", account)
        model.addAttribute("transactions", transactions)
        return "transactions/list"
    }
}
