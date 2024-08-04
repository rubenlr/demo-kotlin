package com.rubenlr.demo.repositories

import com.rubenlr.demo.FakeDataProvider
import com.rubenlr.demo.data.entities.Asset
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.jvm.optionals.getOrNull

@RepositoryTest
class AssetRepositoryTest {

    @Autowired
    private lateinit var assetRepository: AssetRepository

    private lateinit var assets: List<Asset>

    @BeforeEach
    fun setUp() {
        val assetList = FakeDataProvider.getAssets(10)
        assets = assetRepository.saveAllAndFlush(assetList)
    }

    @Test
    fun `should save and find all assets`() {
        assets.forEach { asset ->
            val foundAsset = assetRepository.findById(asset.id).getOrNull()

            assertNotNull(foundAsset, "Missing asset: ${asset.symbol}")
            assertEquals(asset, foundAsset)
        }
    }

    @Test
    fun `should find by symbol`() {
        assets.forEach { asset ->
            val foundAsset = assetRepository.findBySymbol(asset.symbol)

            assertNotNull(foundAsset, "Missing asset: ${asset.symbol}")
            assertEquals(asset, foundAsset)
        }
    }
}