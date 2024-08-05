package com.rubenlr.demo.data

import org.junit.jupiter.api.BeforeEach
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class ExchangeRateProviderTest {

    private lateinit var rateProvider: ExchangeRateProvider

    @BeforeEach
    fun setUp() {
        rateProvider = ExchangeRateProvider()
    }

    @Test
    fun `should proper convert direct FIAT convert`() {
        assertEquals(BigDecimal.valueOf(0.00001873), rateProvider.getRate("EURO", "BTC"))
        assertEquals(BigDecimal.valueOf(2486.10), rateProvider.getRate("EURO", "GOLD"))
        assertEquals(BigDecimal.valueOf(2471.58), rateProvider.getRate("ETH", "EURO"))
        assertEquals(BigDecimal.valueOf(0.00492489), rateProvider.getRate("Apple", "EURO"))
    }

    @Test
    fun `should proper convert from non-FIAT to non-FIAT`() {
        assertEquals(BigDecimal("262.9668062053681359271115488795863"), rateProvider.getRate("BTC", "Apple"))
    }
}