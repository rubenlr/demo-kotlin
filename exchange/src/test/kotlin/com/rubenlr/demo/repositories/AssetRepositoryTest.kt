package com.rubenlr.demo.repositories

import MyPostgresConfiguration
import com.rubenlr.demo.FakeDataProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.jvm.optionals.getOrNull

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ContextConfiguration(classes = [MyPostgresConfiguration::class])
class AssetRepositoryTest {

    @Autowired
    private lateinit var assetRepository: AssetRepository

    @Test
    fun `should save and find all assets`() {
        val assetList = FakeDataProvider.getAssets(10)
        assetRepository.saveAllAndFlush(assetList)

        assetList.forEach { asset ->
            val foundAsset = assetRepository.findById(asset.id).getOrNull()

            assertNotNull(foundAsset, "Missing asset: ${asset.symbol}")
            assertEquals(asset.symbol, foundAsset!!.symbol)
            assertEquals(asset.type, foundAsset.type)
        }
    }
}