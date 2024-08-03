package com.rubenlr.demo.repositories

import com.rubenlr.demo.FakeDataProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.jvm.optionals.getOrNull

@RepositoryTest
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
            assertEquals(asset, foundAsset)
        }
    }
}