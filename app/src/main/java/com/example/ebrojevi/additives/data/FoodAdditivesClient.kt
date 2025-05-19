package com.example.ebrojevi.additives.data

import com.example.ebrojevi.additives.domain.model.FoodAdditive

interface FoodAdditivesClient {
    suspend fun getAllAdditives(): List<FoodAdditive>
}