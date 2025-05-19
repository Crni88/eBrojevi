package com.example.ebrojevi.additives.data

import com.example.ebrojevi.additives.domain.FoodAdditiveRepository
import com.example.ebrojevi.additives.domain.model.FoodAdditive
import com.example.ebrojevi.additives.domain.utils.Resource

class FoodFoodAdditivesRepositoryImpl(
    private val client: FoodAdditivesClient
) : FoodAdditiveRepository {
    override suspend fun getFoodAdditives(): Resource<List<FoodAdditive>> {
        return try {
            Resource.Success(
                client.getAllAdditives()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

}