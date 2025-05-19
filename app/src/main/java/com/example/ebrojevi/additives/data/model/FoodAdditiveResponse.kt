package com.example.ebrojevi.additives.data.model

import com.google.gson.annotations.SerializedName

data class FoodAdditiveResponse(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("type") val type: String,
    @SerializedName("adi") val adi: String,
    @SerializedName("color") val color: String
)
