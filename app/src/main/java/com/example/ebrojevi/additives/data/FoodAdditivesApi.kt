package com.example.ebrojevi.additives.data

import com.example.ebrojevi.additives.data.model.FoodAdditiveResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FoodAdditivesApi {
    @GET("additives/{code}")
    suspend fun getAdditiveByCode(@Path("code") code: String): FoodAdditiveResponse

    @GET("database")
    suspend fun getAllAdditives(): Response<List<FoodAdditiveResponse>>
}
