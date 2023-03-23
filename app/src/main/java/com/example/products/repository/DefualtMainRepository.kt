package com.example.products.repository


import com.example.products.model.Resource
import com.example.products.model.Response
import com.example.products.model.api.Api
import java.lang.Exception
import javax.inject.Inject


class DefualtMainRepository @Inject constructor(
    val api: Api
):MainRepository {
    override suspend fun getProductData(): Resource<Response> {
        return try {
            val response = api.getProductData()
            val result = response.body()
            if (response.isSuccessful && result != null){
                Resource.Success(result)
            }else{
                Resource.Error("An Error occurred")
            }
        }catch (e:Exception){
            println("Api $e")
            Resource.Error("An $e occurred")
        }
    }
}