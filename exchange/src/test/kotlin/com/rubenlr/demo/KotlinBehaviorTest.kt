package com.rubenlr.demo

import com.rubenlr.demo.data.entities.User
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class KotlinBehaviorTest {

    @Test
    fun `assertEquals of data class should compare all elements`() {
        val u1 = User(1, "klklbas", email = "lkbjklajlkasdf")
        val u2 = User(1, "klklbas", email = "lkbjklajlkasdf")
        val u3 = User(2, "klklbas", email = "lkbjklajlkasdf")

        assertEquals(u1, u2)
        assertNotEquals(u1, u3)
    }

    @Test
    fun `assertEquals of a list should compare elements of each item in the list`() {
        val u1 = User(1, "klklbas", email = "lkbjklajlkasdf")
        val u2 = User(1, "klklbas", email = "lkbjklajlkasdf")
        val u3 = User(2, "klklbas", email = "lkbjklajlkasdf")

        assertEquals(listOf(u1, u2), listOf(u1, u2))
        assertEquals(listOf(u1, u2), listOf(u2, u1))
        assertNotEquals(listOf(u1, u3), listOf(u1, u2))
    }
}