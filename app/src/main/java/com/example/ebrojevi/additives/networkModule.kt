package com.example.ebrojevi.additives

import com.example.ebrojevi.additives.data.FoodAdditivesApi
import com.example.ebrojevi.additives.data.FoodAdditivesClient
import com.example.ebrojevi.additives.data.FoodAdditivesClientImpl
import com.example.ebrojevi.additives.data.FoodFoodAdditivesRepositoryImpl
import com.example.ebrojevi.additives.domain.FoodAdditiveRepository
import com.example.ebrojevi.additives.presentation.FoodAdditivesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://ebrojevi-fast-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<FoodAdditivesApi> { get<Retrofit>().create(FoodAdditivesApi::class.java) }
    single<FoodAdditivesClient> { FoodAdditivesClientImpl(get()) }
    single<FoodAdditiveRepository> { FoodFoodAdditivesRepositoryImpl(get()) }
    viewModel { FoodAdditivesViewModel(get()) }
}
