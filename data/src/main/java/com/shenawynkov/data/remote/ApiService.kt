package com.shenawynkov.data.remote

import com.shenawynkov.data.model.TaxisResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    // to use same url as base url
    @GET(".")
    suspend fun getTaxis(
        @Query("p1Lat") firstLat: Double,
        @Query("p1Lon") firstLon: Double,
        @Query("p2Lat") secondLat: Double,
        @Query("p2Lon") secondLon: Double,
    ):TaxisResponse
}