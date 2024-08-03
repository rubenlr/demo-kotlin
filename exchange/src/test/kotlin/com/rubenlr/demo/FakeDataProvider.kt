package com.rubenlr.demo

import com.rubenlr.demo.data.entities.Account
import com.rubenlr.demo.data.entities.Asset
import com.rubenlr.demo.data.entities.AssetType
import com.rubenlr.demo.data.entities.User
import io.github.serpro69.kfaker.Faker
import java.math.BigDecimal
import kotlin.random.Random

object FakeDataProvider {
    private val faker = Faker()

    private fun getUser(userId: Long = 1) = User(userId, faker.name.name(), faker.internet.email())

    fun getUsers(size: Long = 2) = (1..size).map { userId -> getUser(userId) }

    private fun getAsset(assetId: Long = 1) = Asset(
        assetId,
        (1..(3..5).random()).map { faker.random.nextLetter(true) }.joinToString(""),
        AssetType.entries.random()
    )

    fun getAssets(size: Long = 2) = (1..size).map { assetId -> getAsset(assetId) }

    private fun getAccount(user: User, asset: Asset, accountId: Long = 1L, balance: Double = Random.nextDouble(1.0, 1000.0)) =
        Account(accountId, user, asset, BigDecimal.valueOf(balance))

    fun getAccounts(size: Int = 2, users: List<User>, assets: List<Asset>): List<Account> {
        val userAssetList = users.flatMap { user -> assets.map { asset -> user to asset } }.toSet().shuffled()

        return (1..size.coerceAtMost(userAssetList.size)).map { accountId ->
            val (user, asset) = userAssetList[accountId - 1]
            getAccount(user, asset, accountId.toLong())
        }
    }
}