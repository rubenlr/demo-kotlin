package com.rubenlr.demo.data

import com.rubenlr.demo.data.entities.TransactionRate
import com.rubenlr.demo.services.ValidationException
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.MathContext

@Component
class ExchangeRateProvider {
    private val rates = listOf(
        TransactionRate("EURO", "BTC", BigDecimal.valueOf(0.00001873)),
        TransactionRate("EURO", "ETH", BigDecimal.valueOf(0.0004046)),
        TransactionRate("EURO", "Apple", BigDecimal.valueOf(203.05)),
        TransactionRate("EURO", "Tesla", BigDecimal.valueOf(208.31)),
        TransactionRate("EURO", "GOLD", BigDecimal.valueOf(2486.10)),
        TransactionRate("EURO", "SILVER", BigDecimal.valueOf(28.685)),

        TransactionRate("BTC", "EURO", BigDecimal.valueOf(53395.41)),
        TransactionRate("ETH", "EURO", BigDecimal.valueOf(2471.58)),
        TransactionRate("Apple", "EURO", BigDecimal.valueOf(0.00492489)),
        TransactionRate("Tesla", "EURO", BigDecimal.valueOf(0.00480054)),
        TransactionRate("GOLD", "EURO", BigDecimal.valueOf(0.0004022364)),
        TransactionRate("SILVER", "EURO", BigDecimal.valueOf(0.0348614258))
    )

    private val fiatStandard = "EURO"

    fun getRate(fromSymbol: String, toSymbol: String): BigDecimal {

        if (fromSymbol == fiatStandard || toSymbol == fiatStandard) {
            return try {
                rates.first { it.fromSymbol == fromSymbol && it.toSymbol == toSymbol }.rate
            } catch (e: Exception) {
                throw ValidationException("Can't find a rate from $fromSymbol to $toSymbol")
            }
        }

        val rateFromFiat = try {
            rates.first { it.fromSymbol == fromSymbol && it.toSymbol == fiatStandard }.rate
        } catch (e: NoSuchElementException) {
            throw ValidationException("Can't find a rate from $fromSymbol to $fiatStandard")
        }

        val rateToFiat = try {
            rates.first { it.fromSymbol == fiatStandard && it.toSymbol == toSymbol }.rate
        } catch (e: NoSuchElementException) {
            throw ValidationException("Can't find a rate from $fiatStandard to $toSymbol")
        }

        return rateFromFiat.divide(rateToFiat, MathContext.DECIMAL128)
    }
}