package com.rubenlr.demo

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FakeDataProviderTest {
    @Test
    fun `getAccounts should provide a combination of all users and assets`() {
        val users = FakeDataProvider.getUsers(3)
        val assets = FakeDataProvider.getAssets(5)

        val accounts = FakeDataProvider.getAccounts(50, users, assets)

        assertEquals(users.size * assets.size, accounts.size)
    }
}