package com.rubenlr.demo.repositories

import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.Symbol
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AssetRepository : JpaRepository<Asset, Long> {

    @Query("SELECT a FROM Asset a WHERE a.symbol = :symbol")
    fun findBySymbol(@Param("symbol") symbol: Symbol): Optional<Asset>
}