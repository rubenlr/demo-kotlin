package com.rubenlr.demo.repositories

import com.rubenlr.demo.data.entities.Transaction
import org.springframework.beans.factory.annotation.Autowired


@RepositoryTest
class TransactionRepositoryTest {

    @Autowired
    private lateinit var transactionRepository: TransactionRepository

    private lateinit var transactions: List<Transaction>
}