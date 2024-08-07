package com.rubenlr.demo.data.entities

import jakarta.persistence.*

@Entity
@Table(name = "assets")
data class Asset(
    @Id
    var id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var symbol: Symbol,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: AssetType
)

enum class AssetType {
    FIAT,
    CRYPTO,
    STOCK,
    COMMODITIES
}

enum class Symbol {
    EURO,
    BTC,
    ETH,
    Apple,
    Tesla,
    Gold,
    Silver
}