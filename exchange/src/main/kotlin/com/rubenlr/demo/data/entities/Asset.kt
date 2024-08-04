package com.rubenlr.demo.data.entities

import jakarta.persistence.*

@Entity
@Table(name = "assets")
data class Asset(
    @Id
    var id: Long = 0,

    @Column(nullable = false, length = 50)
    var symbol: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: AssetType = AssetType.NONE
)

enum class AssetType {
    NONE,
    FIAT,
    CRYPTO,
    STOCK,
    COMMODITIES
}
