package com.rubenlr.demo.services

import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.repositories.AssetRepository
import org.springframework.stereotype.Service

@Service
class AssetService(private val assetRepository: AssetRepository) {

    fun getAllAssets(): List<Asset> {
        return assetRepository.findAll()
    }
}