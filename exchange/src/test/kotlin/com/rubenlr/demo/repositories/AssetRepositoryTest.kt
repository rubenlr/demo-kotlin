package com.rubenlr.demo.repositories

import MyPostgresConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Testcontainers

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ContextConfiguration(classes = [MyPostgresConfiguration::class])
class AssetRepositoryTest : RepositoryBaseTest() {

    @Test
    fun `should save and find all assets`() {
        val assetList = getSavedAssets()
        val assets = assetsRepository.findAll()
        assertEquals(assetList.size, assets.size)
        assetList.forEach { asset ->
            val foundAsset = assets.find { x -> x.symbol == asset.symbol }

            assertNotNull(foundAsset, "Missing asset: ${asset.symbol}")
            assertEquals(asset.type, foundAsset?.type)
        }
    }
}