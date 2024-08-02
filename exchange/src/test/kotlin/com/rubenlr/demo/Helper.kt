package com.rubenlr.demo

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

object Helper {

    fun randomDecimal(min: Double = 0.0, max: Double) =
        BigDecimal(min + (max - min) * Random.nextDouble()).setScale(2, RoundingMode.HALF_UP)

    fun randomInt(min: Int = 1, max: Int = Int.MAX_VALUE) = min + (max - min) * Random.nextInt()

    fun randomLong(min: Long = 1, max: Long = Long.MAX_VALUE) = min + (max - min) * Random.nextLong()
}