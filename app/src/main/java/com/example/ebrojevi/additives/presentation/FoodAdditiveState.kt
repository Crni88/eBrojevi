package com.example.ebrojevi.additives.presentation

import com.example.ebrojevi.additives.domain.model.FoodAdditive

data class FoodAdditiveState(
    val additives: List<FoodAdditive>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)