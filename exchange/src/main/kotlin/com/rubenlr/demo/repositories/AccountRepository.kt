package com.rubenlr.demo.repositories

import com.rubenlr.demo.data.entities.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a JOIN FETCH a.user JOIN FETCH a.asset WHERE a.id = :id")
    override fun findById(@Param("id") id: Long): Optional<Account>

    @Query("SELECT a FROM Account a JOIN FETCH a.user JOIN FETCH a.asset")
    override fun findAll(): List<Account>

    @Query("SELECT a FROM Account a JOIN FETCH a.user JOIN FETCH a.asset WHERE a.user.id = :userId")
    fun findByUserId(@Param("userId") userId: Long): List<Account>

    @Query("SELECT a FROM Account a JOIN FETCH a.user JOIN FETCH a.asset WHERE a.user.id = :userId AND a.asset.id = :assetId")
    fun findByUserIdAndAssetId(@Param("userId") userId: Long, @Param("assetId") assetId: Long): Optional<Account>
}
