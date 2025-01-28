package hr.vpetrina.music.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL = "https://api.jamendo.com/v3.0/";

interface SongsApi {
    @GET("tracks/?client_id=32556c81")
    fun fetchItems(
        @Query("limit") limit: Int = 10,
        @Query("namesearch") name: String) : Call<ApiResponse>
}