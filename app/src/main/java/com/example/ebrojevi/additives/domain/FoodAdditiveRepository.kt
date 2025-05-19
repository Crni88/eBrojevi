package com.example.ebrojevi.additives.domain

import com.example.ebrojevi.additives.domain.model.FoodAdditive
import com.example.ebrojevi.additives.domain.utils.Resource

interface FoodAdditiveRepository {
    suspend fun getFoodAdditives(): Resource<List<FoodAdditive>>
}