package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.HostelRepository
import com.example.ui.HostelListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Hostel Search", appName)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `search by location Ahmedabad returns matching hostels`() = runTest {
        val viewModel = HostelListViewModel()
        viewModel.onSearchQueryChange("Ahmedabad")
        val state = viewModel.uiState.first { it.searchQuery == "Ahmedabad" }
        assertTrue(state.hostels.isNotEmpty())
        assertTrue(state.hostels.all { it.address.contains("Ahmedabad", ignoreCase = true) || it.name.contains("Ahmedabad", ignoreCase = true) })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `search by location Maninagar returns Maninagar hostels`() = runTest {
        val viewModel = HostelListViewModel()
        viewModel.onSearchQueryChange("Maninagar")
        val state = viewModel.uiState.first { it.searchQuery == "Maninagar" }
        assertTrue(state.hostels.isNotEmpty())
        assertTrue(state.hostels.all { it.address.contains("Maninagar", ignoreCase = true) || it.name.contains("Maninagar", ignoreCase = true) })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `search by price 5000 returns 5000 hostels`() = runTest {
        val viewModel = HostelListViewModel()
        viewModel.onSearchQueryChange("5000")
        val state = viewModel.uiState.first { it.searchQuery == "5000" }
        assertTrue(state.hostels.isNotEmpty())
        assertTrue(state.hostels.all { it.monthlyPrice == 5000 || it.monthlyPrice.toString().contains("5000") })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `search by price 7000 returns 7000 hostels`() = runTest {
        val viewModel = HostelListViewModel()
        viewModel.onSearchQueryChange("7000")
        val state = viewModel.uiState.first { it.searchQuery == "7000" }
        assertTrue(state.hostels.isNotEmpty())
        assertTrue(state.hostels.all { it.monthlyPrice == 7000 || it.monthlyPrice.toString().contains("7000") })
    }

    @Test
    fun `sample repository has at least 20 hostels`() {
        assertTrue(HostelRepository.sampleHostels.size >= 20)
    }
}
