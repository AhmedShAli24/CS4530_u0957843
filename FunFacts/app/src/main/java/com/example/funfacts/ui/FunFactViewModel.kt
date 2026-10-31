package com.example.funfacts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.funfacts.data.FunFactRepository
import com.example.funfacts.data.local.FunFact
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FunFactViewModel(
    private val repository: FunFactRepository,
) : ViewModel() {

    // ---- Search ----
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    /**
     * Every time the query changes, switch to a new Room query (flatMapLatest
     * cancels the previous one). Room's Flow also re-emits when a fact is
     * inserted, so this one stream covers both "search as you type" and
     * "new fact appears immediately".
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val facts: StateFlow<List<FunFact>> = _query
        .flatMapLatest { repository.searchFacts(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    // ---- Fetch state ----
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun fetchNewFact() {
        if (_isLoading.value) return // ignore double taps
        viewModelScope.launch {
            _isLoading.value = true
            repository.fetchAndSaveRandomFact()
                .onSuccess { newFact ->
                    // If a search filter would hide the new fact, clear it so
                    // the user actually sees what they just fetched.
                    val q = _query.value.trim()
                    if (q.isNotEmpty() && !newFact.text.contains(q, ignoreCase = true)) {
                        _query.value = ""
                    }
                }
                .onFailure { e ->
                    _errorMessage.value = "Couldn't fetch a fact: ${e.message ?: "unknown error"}"
                }
            _isLoading.value = false
        }
    }

    fun deleteFact(fact: FunFact) {
        viewModelScope.launch { repository.deleteFact(fact.id) }
    }

    fun errorShown() {
        _errorMessage.update { null }
    }
}

/**
 * Factory so the system can build a FunFactViewModel that needs a constructor
 * argument. Without it, ViewModelProvider only knows how to call a no-arg constructor.
 */
class FunFactViewModelFactory(
    private val repository: FunFactRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FunFactViewModel::class.java)) {
            return FunFactViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
