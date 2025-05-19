package com.example.ebrojevi.additives.data

import com.example.ebrojevi.additives.data.mapper.toDomainModels
import com.example.ebrojevi.additives.domain.model.FoodAdditive

class FoodAdditivesClientImpl(private val api: FoodAdditivesApi) : FoodAdditivesClient {
    override suspend fun getAllAdditives(): List<FoodAdditive> {
        val result = api.getAllAdditives().body() ?: emptyList()
        return result.toDomainModels()
    }
}