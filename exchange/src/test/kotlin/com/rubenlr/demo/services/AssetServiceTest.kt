package com.rubenlr.demo.services

import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.AssetType
import com.rubenlr.demo.repositories.AssetRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class AssetServiceTest {

    @MockK
    private lateinit var assetRepository: AssetRepository

    private lateinit var assetService: AssetService

    private lateinit var assets: List<Asset>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        assetService = AssetService(assetRepository)
        assets = listOf(Asset(1, "BTC", AssetType.CRYPTO), Asset(2, "ETH", AssetType.CRYPTO))
    }

    @Test
    fun `should return all assets`() {
        every { assetRepository.findAll() } returns assets

        val result = assetService.getAllAssets()

        assertEquals(assets, result)

        verify(exactly = 1) { assetRepository.findAll() }
    }
}

