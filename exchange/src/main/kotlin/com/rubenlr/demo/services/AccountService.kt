package com.rubenlr.demo.services

import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.repositories.AccountRepository
import com.rubenlr.demo.repositories.AssetRepository
import org.springframework.stereotype.Service

@Service
class AccountService(private val accountRepository: AccountRepository, private val assetRepository: AssetRepository) {

    fun getAssetsWithAccounts(userId: Long): Map<Asset, Account?> {
        val accounts = accountRepository.findByUserId(userId).associateBy { it.asset.id }
        return assetRepository.findAll().associateWith { accounts[it.id] }
    }
}
