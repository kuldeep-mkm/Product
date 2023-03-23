package com.example.products.model.api

import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("products")
    suspend fun getProductData(): Response<com.example.products.model.Response>
}