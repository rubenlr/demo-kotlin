package com.rubenlr.demo

import com.rubenlr.demo.data.entities.*
import com.rubenlr.demo.repositories.AccountRepository
import com.rubenlr.demo.repositories.AssetRepository
import com.rubenlr.demo.repositories.TransactionRepository
import com.rubenlr.demo.repositories.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import java.math.BigDecimal
import java.time.OffsetDateTime

@Configuration
class DataSeeder {

    @Bean
    @Order(1)
    fun initializeUsers(userRepository: UserRepository) = ApplicationRunner {
        if (userRepository.count() == 0L) {
            val users = listOf(
                User(name = "John Doe", email = "john.doe@example.com"),
                User(name = "Jane Doe", email = "jane.doe@example.com")
            )
            userRepository.saveAll(users)
        }
    }

    @Bean
    @Order(2)
    fun initializeAssets(assetRepository: AssetRepository) = ApplicationRunner {
        if (assetRepository.count() == 0L) {
            val assets = listOf(
                Asset(1, Symbol.EURO, AssetType.FIAT),
                Asset(2, Symbol.BTC, AssetType.CRYPTO),
                Asset(3, Symbol.ETH, AssetType.CRYPTO),
                Asset(4, Symbol.Apple, AssetType.STOCK),
                Asset(5, Symbol.Tesla, AssetType.STOCK),
                Asset(6, Symbol.Gold, AssetType.COMMODITIES),
                Asset(7, Symbol.Silver, AssetType.COMMODITIES),
            )
            assetRepository.saveAll(assets)
        }
    }

    @Bean
    @Order(3)
    fun addAccounts(
        userRepository: UserRepository,
        assetRepository: AssetRepository,
        accountRepository: AccountRepository
    ) = ApplicationRunner {
        if (accountRepository.count() == 0L) {
            val savedUsers = userRepository.findAll()
            val savedAssets = assetRepository.findAll().take(5)

            val accounts = savedUsers.flatMap { user ->
                savedAssets.map { asset ->
                    Account(
                        user = user,
                        asset = asset,
                        balance = if (asset.symbol == Symbol.EURO) BigDecimal.valueOf(100000) else BigDecimal.ZERO
                    )
                }
            }
            accountRepository.saveAll(accounts)
        }
    }

    @Bean
    @Order(4)
    fun addDemoTransactions(
        accountRepository: AccountRepository,
        transactionRepository: TransactionRepository
    ) = ApplicationRunner {
        if (transactionRepository.count() == 0L) {
            val accounts = accountRepository.findAll();
            val userIds = accounts.map { it.user.id }.toHashSet()

            userIds.forEach { userId ->
                val userAccounts = accounts.filter { it.user.id == userId }
                userAccounts.forEach { accountFrom ->
                    // Can't transfer from to the same account
                    userAccounts.filter { it.id != accountFrom.id }.forEach { toAccount ->
                        val transaction = Transaction(
                            fromAccount = accountFrom,
                            fromValue = BigDecimal.valueOf(1.0),
                            type = TransactionType.EXCHANGE,
                            toAccount = toAccount,
                            toValue = BigDecimal.valueOf(1.1),
                            executedAt = OffsetDateTime.now()
                        )
                        transactionRepository.save(transaction)
                    }
                }
            }
        }
    }
}