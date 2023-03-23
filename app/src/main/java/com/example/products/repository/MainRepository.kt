package com.example.products.repository

import com.example.products.model.Resource
import com.example.products.model.Response


interface MainRepository {
    suspend fun getProductData(): Resource<Response>
}