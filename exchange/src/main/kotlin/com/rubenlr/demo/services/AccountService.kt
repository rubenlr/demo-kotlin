package com.rubenlr.demo.services

import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.Symbol
import com.rubenlr.demo.repositories.AccountRepository
import com.rubenlr.demo.repositories.AssetRepository
import com.rubenlr.demo.repositories.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val assetRepository: AssetRepository,
    private val userRepository: UserRepository
) {

    fun getById(accountId: Long) = accountRepository.findById(accountId)

    fun getAssetsWithAccounts(userId: Long): Map<Asset, Account?> {
        val accounts = accountRepository.findByUserId(userId).associateBy { it.asset.id }
        return assetRepository.findAll().associateWith { accounts[it.id] }
    }

    fun save(userId: Long, symbol: Symbol): Account {
        val user = userRepository.findById(userId).orElseThrow {
            throw InvalidInputException("User $userId not found")
        }
        val asset = assetRepository.findBySymbol(symbol).orElseThrow {
            throw InvalidInputException("Asset $symbol not found")
        }

        if (accountRepository.findByUserIdAndAssetId(user.id, asset.id).isPresent)
            throw RecordAlreadyExistsException("Account already exists for User $userId and Asset $symbol")

        val account = Account(user = user, asset = asset, balance = BigDecimal.ZERO)

        return accountRepository.saveAndFlush(account)
    }
}
