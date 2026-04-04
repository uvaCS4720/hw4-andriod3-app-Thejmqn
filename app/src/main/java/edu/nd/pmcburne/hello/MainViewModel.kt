package edu.nd.pmcburne.hello

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUIState(
    val counterValue: Int,
    val placemarks: List<Placemark> = emptyList(),
    val selectedTag: String = "core",
    val errorMessage: String? = null
)

class MainViewModel(
    private val repository: MapRepository,
    val initialCounterValue: Int = 0
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUIState(initialCounterValue))
    val uiState: StateFlow<MainUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allPlacemarks.collect { list ->
                _uiState.update { it.copy(placemarks = list) }
            }
        }
        
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            try {
                repository.syncPlacemarks()
                _uiState.update { it.copy(errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "${e.message}. Defaulting to database." ?: "Unknown error occurred") }
            }
        }
    }

    fun updateSelectedTag(tag: String) {
        _uiState.update { it.copy(selectedTag = tag) }
    }

    fun incrementCounter() {
        _uiState.update { currentState ->
            currentState.copy(counterValue = currentState.counterValue + 1)
        }
    }

    fun decrementCounter() {
        _uiState.update { currentState ->
            currentState.copy(counterValue = currentState.counterValue - 1)
        }
    }

    fun resetCounter() {
        _uiState.update { currentState ->
            currentState.copy(counterValue = 0)
        }
    }

    val isDecrementEnabled: Boolean
        get() = _uiState.value.counterValue > 0
    val isResetEnabled: Boolean
        get() = _uiState.value.counterValue > 0

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return MainViewModel(
                    (application as MapApplication).repository
                ) as T
            }
        }
    }
}
