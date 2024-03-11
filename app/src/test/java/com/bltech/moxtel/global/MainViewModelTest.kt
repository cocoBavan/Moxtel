package com.bltech.moxtel.global

import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MainViewModelTest {
    @Test
    fun `when you set a title to the title flow should emit it`() = runTest {
        val model = MainViewModel()
        val expected = "SOME TITLE"
        model.setTitle(expected)
        val firstItem = model.titleFlow.first()
        assert(firstItem == expected)
    }

    @Test
    fun `when you set titles to the title flow should emit it in order`() = runTest {
        val model = MainViewModel()
        model.titleFlow.test {
            awaitItem() // Initial Value is ignored
            model.setTitle("a")
            assertEquals("a", awaitItem())
            model.setTitle("b")
            assertEquals("b", awaitItem())
            model.setTitle("c")
            assertEquals("c", awaitItem())
        }
    }
}
