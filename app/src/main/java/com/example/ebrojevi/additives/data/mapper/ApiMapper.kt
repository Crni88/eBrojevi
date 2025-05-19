package com.example.ebrojevi.additives.data.mapper

import com.example.ebrojevi.additives.domain.model.FoodAdditive
import com.example.ebrojevi.additives.data.model.FoodAdditiveResponse

fun FoodAdditiveResponse.toDomainModel(): FoodAdditive {
    return FoodAdditive(
        code = code,
        name = name,
        type = type,
        adi = adi,
        color = color,
        description = description
    )
}

fun List<FoodAdditiveResponse>.toDomainModels(): List<FoodAdditive> {
    return this.map { it.toDomainModel() }
}
