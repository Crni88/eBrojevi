package com.example.ebrojevi.additives.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ebrojevi.additives.domain.FoodAdditiveRepository
import com.example.ebrojevi.additives.domain.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FoodAdditivesViewModel(
    private val repository: FoodAdditiveRepository
) : ViewModel() {
    private val _state = MutableStateFlow(FoodAdditiveState())
    val state = _state.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true,
                error = null
            ) }

            when (val result = repository.getFoodAdditives()) {
                is Resource.Success -> {
                    _state.update { it.copy(
                        additives = result.data,
                        isLoading = false,
                        error = null
                    ) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(
                        additives = null,
                        isLoading = false,
                        error = result.message
                    ) }
                }
            }
        }
    }
}
