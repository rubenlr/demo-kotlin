package com.rubenlr.demo.data

import com.rubenlr.demo.data.entities.Symbol
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
        assertEquals(BigDecimal("0.00001872820154391547887730424768720757"), rateProvider.getRate(Symbol.EURO, Symbol.BTC))
        assertEquals(BigDecimal("0.0004022364345762439161739270343107679"), rateProvider.getRate(Symbol.EURO, Symbol.Gold))
        assertEquals(BigDecimal("2471.58"), rateProvider.getRate(Symbol.ETH, Symbol.EURO))
        assertEquals(BigDecimal("203.05"), rateProvider.getRate(Symbol.Apple, Symbol.EURO))
    }

    @Test
    fun `should proper convert from non-FIAT to non-FIAT`() {
        assertEquals(BigDecimal("9.223443262209051404729991473630913E-8"), rateProvider.getRate(Symbol.BTC, Symbol.Apple))
    }
}