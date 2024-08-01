package com.rubenlr.demo.repositories

import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.AssetType
import com.rubenlr.demo.data.entities.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
open class RepositoryBaseTest {

    companion object {
        @Container
        val postgresContainer = PostgreSQLContainer<Nothing>("postgres:16-alpine").apply {
            withDatabaseName("testdb")
            withUsername("test")
            withPassword("test")
            //withExposedPorts(5432) // Ensure the port is exposed
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerPgProperties(registry: DynamicPropertyRegistry) {
            postgresContainer.start() // Start the container
            registry.add("spring.datasource.url") { postgresContainer.jdbcUrl }
            registry.add("spring.datasource.username") { postgresContainer.username }
            registry.add("spring.datasource.password") { postgresContainer.password }
        }
    }

    @Test
    fun `should start container`() {
        assertNotNull(postgresContainer.containerInfo.state.running)
        assertTrue(postgresContainer.containerInfo.state.running ?: false)
    }

    protected fun randomDecimal(min: Double = 0.0, max: Double) =
        BigDecimal(min + (max - min) * Random.nextDouble()).setScale(2, RoundingMode.HALF_UP)

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
