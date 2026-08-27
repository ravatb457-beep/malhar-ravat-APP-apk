package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Hostel
import com.example.data.HostelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HostelListUiState(
    val searchQuery: String = "",
    val hostels: List<Hostel> = emptyList(),
    val totalCount: Int = 0,
    val resultCount: Int = 0
)

class HostelListViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _allHostels = MutableStateFlow(HostelRepository.sampleHostels)

    val uiState: StateFlow<HostelListUiState> = combine(_allHostels, _searchQuery) { hostels, query ->
        val trimmedQuery = query.trim()
        val filtered = if (trimmedQuery.isEmpty()) {
            hostels
        } else {
            val normalizedNumericQuery = trimmedQuery
                .replace("₹", "")
                .replace("rs", "", ignoreCase = true)
                .replace("inr", "", ignoreCase = true)
                .replace(",", "")
                .replace(" ", "")

            hostels.filter { hostel ->
                val matchesName = hostel.name.contains(trimmedQuery, ignoreCase = true)
                val matchesAddress = hostel.address.contains(trimmedQuery, ignoreCase = true)
                val matchesInfo = hostel.basicInfo.contains(trimmedQuery, ignoreCase = true)
                val matchesFormattedPrice = hostel.formattedPrice.contains(trimmedQuery, ignoreCase = true)
                val matchesNumericPrice = if (normalizedNumericQuery.isNotEmpty() && normalizedNumericQuery.all { it.isDigit() }) {
                    hostel.monthlyPrice.toString().contains(normalizedNumericQuery)
                } else {
                    false
                }

                matchesName || matchesAddress || matchesInfo || matchesFormattedPrice || matchesNumericPrice
            }
        }

        HostelListUiState(
            searchQuery = query,
            hostels = filtered,
            totalCount = hostels.size,
            resultCount = filtered.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HostelListUiState(
            searchQuery = "",
            hostels = HostelRepository.sampleHostels,
            totalCount = HostelRepository.sampleHostels.size,
            resultCount = HostelRepository.sampleHostels.size
        )
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }
}
