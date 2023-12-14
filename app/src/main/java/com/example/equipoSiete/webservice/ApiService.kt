package com.example.equipoSiete.webservice

import com.example.equipoSiete.model.Product
import com.example.equipoSiete.utils.Constants.END_POINT
import retrofit2.http.GET

interface ApiService {
    @GET(END_POINT)
    suspend fun getProducts(): MutableList<Product>
}