package com.pucetec.geomed

import com.pucetec.users.UsersApplication
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class UsersApplicationTests {

    @Test
    fun testAppInstantiation() {
        val app = UsersApplication()
        assertNotNull(app)
    }
}
