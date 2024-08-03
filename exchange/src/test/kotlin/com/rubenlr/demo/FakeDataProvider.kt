package com.rubenlr.demo

import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.AssetType
import com.rubenlr.demo.data.entities.User
import net.andreinc.mockneat.MockNeat
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

object FakeDataProvider {
    private val mockNeat = MockNeat.threadLocal()

    private fun getUser(userId: Long = 1) = User(userId, mockNeat.names().first().get(), mockNeat.emails().get())

    fun getUsers(size: Long = 2) = (1..size).map { userId -> getUser(userId) }

    private fun getAsset(assetId: Long = 1) = Asset(
        assetId,
        (1..(3..5).random()).map { mockNeat.chars().alphaNumeric().get() }.joinToString("").uppercase(),
        AssetType.entries.random()
    )

    fun getAssets(size: Long = 2) = (1..size).map { assetId -> getAsset(assetId) }

    private fun getAccount(user: User, asset: Asset, accountId: Long = 1L, balance: Double = Random.nextDouble(1.0, 1000.0)) =
        Account(accountId, user, asset, BigDecimal.valueOf(balance).setScale(2, RoundingMode.HALF_UP))

    fun getAccounts(users: List<User>, assets: List<Asset>): List<Account> {
        val userAssetList = users.flatMap { user -> assets.map { asset -> user to asset } }.toSet().shuffled()

        return (1..userAssetList.size).map { accountId ->
            val (user, asset) = userAssetList[accountId - 1]
            getAccount(user, asset, accountId.toLong())
        }
    }
}