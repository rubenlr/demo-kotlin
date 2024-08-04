package com.rubenlr.demo

import com.rubenlr.demo.data.entities.*
import io.bloco.faker.Faker
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.OffsetDateTime
import kotlin.random.Random

object FakeDataProvider {
    private val faker = Faker()

    private fun nextDecimal(min: Double = 1.0, max: Double = 10000.0) =
        BigDecimal.valueOf(Random.nextDouble(min, max)).setScale(2, RoundingMode.HALF_UP)

    private fun getUser(userId: Long = 1) = User(userId, faker.name.name(), faker.internet.email())

    fun getUsers(size: Long = 2) = (1..size).map { userId -> getUser(userId) }

    private fun getAsset(assetId: Long = 1) = Asset(
        assetId,
        faker.company.name().filter { it.isLetterOrDigit() }.take(Random.nextInt(3, 5)).uppercase(),
        AssetType.entries.random()
    )

    fun getAssets(size: Long = 2) = (1..size).map { assetId -> getAsset(assetId) }

    private fun getAccount(user: User, asset: Asset, accountId: Long = 1L) =
        Account(accountId, user, asset, nextDecimal())

    fun getAccounts(users: List<User>, assets: List<Asset>): List<Account> {
        val userAssetList = users.flatMap { user -> assets.map { asset -> user to asset } }.toSet().shuffled()

        return (1..userAssetList.size).map { accountId ->
            val (user, asset) = userAssetList[accountId - 1]
            getAccount(user, asset, accountId.toLong())
        }
    }

    private fun getTransaction(from: Account, to: Account, transactionId: Long) =
        Transaction(
            transactionId,
            from,
            nextDecimal(),
            TransactionType.EXCHANGE,
            to,
            nextDecimal(),
            OffsetDateTime.now()
        )

    fun getTransactions(accounts: List<Account>): List<Transaction> {
        var transactionId = 1L
        val transactions = mutableListOf<Transaction>()

        accounts.shuffled().forEach { from ->
            // Don't want `from` and `to` to be equal
            accounts.filter { it != from }.shuffled().forEach { to ->
                repeat(Random.nextInt(2, 5)) {
                    transactions.add(getTransaction(from, to, transactionId++))
                }
            }
        }

        return transactions
    }

    data class TransactionDto(
        val users: List<User>,
        val assets: List<Asset>,
        val accounts: List<Account>,
        val transactions: List<Transaction>
    )

    fun getTransactions(usersSize: Long, assetsSize: Long): TransactionDto {
        val users = getUsers(usersSize)
        val assets = getAssets(assetsSize)
        val accounts = getAccounts(users, assets)
        val transactions = getTransactions(accounts)

        return TransactionDto(users, assets, accounts, transactions)
    }
}