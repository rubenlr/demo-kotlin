package com.rubenlr.demo.repositories

import com.rubenlr.demo.data.entities.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long> {

    fun findByUserId(userId: Long): List<Account>
}