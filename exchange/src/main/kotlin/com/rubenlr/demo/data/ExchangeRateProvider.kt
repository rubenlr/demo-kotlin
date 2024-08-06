package com.rubenlr.demo.data

import com.rubenlr.demo.data.entities.Symbol
import com.rubenlr.demo.services.ValidationException
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext

@Component
class ExchangeRateProvider {
    // Considering price won't be zero - never.
    private val prices = hashMapOf(
        Symbol.BTC to 53395.41,
        Symbol.ETH to 2471.58,
        Symbol.Apple to 203.05,
        Symbol.Tesla to 208.31,
        Symbol.Gold to 2486.10,
        Symbol.Silver to 28.685
    )

    private val fiatStandard = Symbol.EURO

    fun getRate(fromSymbol: Symbol, toSymbol: Symbol): BigDecimal {
        if (fromSymbol == fiatStandard) {
            try {
                val rate = BigDecimal.valueOf(prices.getValue(toSymbol))
                return BigDecimal.valueOf(1).divide(rate, MathContext.DECIMAL128)
            } catch (ex: Exception) {
                throw ValidationException("Can't find a rate from $fromSymbol to $toSymbol")
            }
        }
        if (toSymbol == fiatStandard) {
            try {
                return BigDecimal.valueOf(prices.getValue(fromSymbol))
            } catch (e: Exception) {
                throw ValidationException("Can't find a rate from $fromSymbol to $toSymbol")
            }
        }

        val rateToFiat = getRate(fromSymbol, fiatStandard)
        val rateFromFiat = getRate(fiatStandard, toSymbol)
        return rateFromFiat.divide(rateToFiat, MathContext.DECIMAL128)
    }
}