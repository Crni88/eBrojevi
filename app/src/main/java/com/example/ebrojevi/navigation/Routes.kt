package com.example.ebrojevi.navigation

import java.net.URLEncoder

object Routes {
    const val CAMERA_ROUTE = "camera"
    const val LOADING_ROUTE = "loading"
    const val ADDITIVES_ROUTE = "additives"
    fun loadingRoute(queryString: String): String {
        val encodedQuery = URLEncoder.encode(queryString, "UTF-8")
        return "$LOADING_ROUTE/$encodedQuery"
    }
}