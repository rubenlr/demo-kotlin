package com.rubenlr.demo.repositories

import com.rubenlr.demo.data.entities.Asset
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AssetRepository : JpaRepository<Asset, Long>