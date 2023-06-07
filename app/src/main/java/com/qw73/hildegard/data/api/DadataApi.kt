package com.qw73.hildegard.data.api

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

import javax.inject.Inject

interface DadataApi {
    @GET("address")
    fun getAddressSuggestions(
        @Query("query") query: String,
        @Query("count") count: Int
    ): Call<AddressResponse>
}

data class AddressResponse(val suggestions: List<AddressSuggestion>)

data class AddressSuggestion(val value: String)