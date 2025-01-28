package hr.vpetrina.music.api

import com.google.gson.annotations.SerializedName

data class SongItem (
    @SerializedName("id") val id : String,
    @SerializedName("image") val imageUrl : String,
    @SerializedName("name") val title : String
)

data class ApiResponse(
   @SerializedName("headers") val headers: Headers,
   @SerializedName("results") val results: List<SongItem>
)

data class Headers(
    val status: String,
    val code: Int,
    val errorMessage: String?,
    val warnings: String?,
    val resultsCount: Int,
    val next: String?
)


