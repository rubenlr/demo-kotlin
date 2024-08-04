package com.rubenlr.demo

import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.AssetType
import com.rubenlr.demo.data.entities.User
import com.rubenlr.demo.repositories.AccountRepository
import com.rubenlr.demo.repositories.AssetRepository
import com.rubenlr.demo.repositories.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import java.math.BigDecimal

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
                Asset(1, "EURO", AssetType.FIAT),
                Asset(2, "BTC", AssetType.CRYPTO),
                Asset(3, "ETH", AssetType.CRYPTO),
                Asset(4, "Apple", AssetType.STOCK),
                Asset(5, "Tesla", AssetType.STOCK),
                Asset(6, "GOLD", AssetType.COMMODITIES),
                Asset(7, "SILVER", AssetType.COMMODITIES),
            )
            assetRepository.saveAll(assets)
        }
    }

    @Bean
    @Order(3)
    fun accountsAssets(
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
                        balance = if (asset.symbol == "EURO") BigDecimal.valueOf(1000) else BigDecimal.ZERO
                    )
                }
            }
            accountRepository.saveAll(accounts)
        }
    }
}