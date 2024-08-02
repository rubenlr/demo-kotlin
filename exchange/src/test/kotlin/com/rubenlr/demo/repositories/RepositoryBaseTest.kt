package com.rubenlr.demo.repositories

import MyPostgresConfiguration
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.AssetType
import com.rubenlr.demo.data.entities.User
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ContextConfiguration(classes = [MyPostgresConfiguration::class])
open class RepositoryBaseTest {


    @Autowired
    protected lateinit var assetsRepository: AssetRepository

    private val assetList = listOf(
        Asset(1, "EURO", AssetType.FIAT),
        Asset(2, "BTC", AssetType.CRYPTO),
        Asset(3, "ETH", AssetType.CRYPTO),
        Asset(4, "Apple", AssetType.STOCK),
        Asset(5, "Tesla", AssetType.STOCK),
        Asset(6, "GOLD", AssetType.COMMODITIES),
        Asset(7, "SILVER", AssetType.COMMODITIES),
    )

    protected fun getSavedAssets(): MutableList<Asset> = assetsRepository.saveAllAndFlush(assetList)

    @Autowired
    protected lateinit var usersRepository: UserRepository

    private val userList = listOf(
        User(name = "John", email = "john@test.com"),
        User(name = "Jane", email = "jane@test.com")
    )

    protected fun getSavedUsers(): MutableList<User> = usersRepository.saveAllAndFlush(userList)
}
