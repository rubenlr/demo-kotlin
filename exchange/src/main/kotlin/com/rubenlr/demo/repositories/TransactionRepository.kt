package com.rubenlr.demo.repositories

import com.rubenlr.demo.data.entities.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {

    @Query(
        "SELECT t FROM Transaction t " +
                "JOIN FETCH t.fromAccount " +
                "JOIN FETCH t.fromAccount.user " +
                "JOIN FETCH t.fromAccount.asset " +
                "JOIN FETCH t.toAccount " +
                "JOIN FETCH t.toAccount.user " +
                "JOIN FETCH t.toAccount.asset " +
                "WHERE t.fromAccount.id = :accountId OR t.toAccount.id = :accountId"
    )
    fun findAllByAccountId(@Param("accountId") accountId: Long): List<Transaction>
}