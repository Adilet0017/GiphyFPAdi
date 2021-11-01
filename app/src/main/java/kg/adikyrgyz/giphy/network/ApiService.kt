package kg.adikyrgyz.giphy.network

import io.reactivex.Single
import kg.adikyrgyz.giphy.network.model.TrendingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("trending")
    fun getTrendingGifs(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ):Single<TrendingResponse>

    @GET("search")
    fun searchGifs(
        @Query("api_key") apiKey: String,
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ):Single<TrendingResponse>
}