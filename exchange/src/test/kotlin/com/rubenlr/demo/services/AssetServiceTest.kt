package com.rubenlr.demo.services

import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.AssetType
import com.rubenlr.demo.repositories.AssetRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
class AssetServiceTest {

    @Mock
    private lateinit var assetRepository: AssetRepository

    @InjectMocks
    private lateinit var assetService: AssetService

    private lateinit var assets: List<Asset>

    @BeforeEach
    fun setUp() {
        assets = listOf(Asset(1, "BTC", AssetType.CRYPTO), Asset(2, "ETH", AssetType.CRYPTO))
    }

    @Test
    fun `should return all assets`() {
        `when`(assetRepository.findAll()).thenReturn(assets)

        val result = assetService.getAllAssets()

        assertEquals(assets.size, result.size)

        assets.forEach { asset ->
            val actualAsset = result.first { x -> x.id == asset.id }
            assertNotNull(actualAsset)
            assertEquals(asset.type, actualAsset.type)
            assertEquals(asset.symbol, actualAsset.symbol)
        }

        verify(assetRepository, times(1)).findAll()
    }
}

